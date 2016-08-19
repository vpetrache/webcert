/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* global pages, protractor, logger, ursprungligtIntyg, Promise, JSON */

'use strict';
var fk7263Utkast = pages.intyg.fk['7263'].utkast;
var db = require('./dbActions');
var tsBasintygtPage = pages.intyg.ts.bas.intyg;
var statusuppdateringarRows;

var handelseRegex = /(HAN\d{1,2})/g;


function selectTable(handelsekod) {
    var res = handelsekod.match(handelseRegex);
    if (res) {
        if (res.length > 1) {
            logger.error('ERROR: More than one "händelse" found.');
            return;
        }
        // console.log('Database: webcert_requests.requests');
        return 'webcert_requests.requests';
    } else {
        console.log('Database: webcert_requests.statusupdates_2');
        return 'webcert_requests.statusupdates_2';
    }
}

function selectExtension(handelsekod) {
    var res = handelsekod.match(handelseRegex);
    if (res) {
        if (res.length > 1) {
            throw ('ERROR: More than one "händelse" found. Cannot select determine extension');
        }
        return 'utlatandeExtension';
    } else {
        return 'intygsExtension';
    }
}

function selectHandelseTidName(handelsekod) {
    var res = handelsekod.match(handelseRegex);
    if (res) {
        return 'handelsetidpunkt';
    } else {
        return 'handelseTid';
    }
}


function getNotificationEntries(intygsId, handelsekod, numEvents) {
    // Select table and extension based on if 'händelse' contains the pattern HAN{1-2digit} or NOT
    var table = selectTable(handelsekod);
    var extensionType = selectExtension(handelsekod);
    var handelseTidName = selectHandelseTidName(handelsekod);

    var databaseTable = table;
    var query =
        'SELECT ' + extensionType + ', handelseKod,antalFragor,antalHanteradeFragor,antalSvar,antalHanteradeSvar, ' + handelseTidName +
        ' FROM ' + databaseTable +
        ' WHERE ' + databaseTable + '.handelseKod = "' + handelsekod + '"' +
        ' AND ' + databaseTable + '.' + extensionType + ' = "' + intygsId + '"' +
        ' ORDER BY ' + handelseTidName + ' DESC;';

    console.log('query: ' + query);

    var conn = db.makeConnection();
    conn.connect();
    var promise = new Promise(function(resolve, reject) {
        conn.query(query,
            function(err, rows, fields) {
                conn.end();
                if (err) {
                    reject(err);
                    // } else if (rows.length !== numEvents) {
                    //     // console.log('FEL, Antal händelser i db: ' + rows[0].Counter + ' (' + numEvents + ')');
                    //     resolve();
                } else {
                    console.log('Antal händelser i db ' + rows.length + '(' + numEvents + ')');
                    resolve(rows);
                }
            });
    });
    return promise;
}

function waitForCount(intygsId, handelsekod, numEvents, cb) {
    var intervall = 5000;

    getNotificationEntries(intygsId, handelsekod, numEvents).then(function(result) {
        if (result.length >= numEvents) {
            console.log('Hittade rader: ' + JSON.stringify(result));
            statusuppdateringarRows = result;
            cb();
        } else {
            console.log('Hittade färre än ' + numEvents + 'rader i databasen');
            console.log('Ny kontroll sker efter ' + intervall + 'ms');
            setTimeout(function() {
                waitForCount(intygsId, handelsekod, numEvents, cb);
            }, intervall);
        }

    }, function(err) {
        cb(err);

    });
}

module.exports = function() {

    this.Then(/^ska intygsutkastets status vara "([^"]*)"$/, function(statustext, callback) {
        expect(tsBasintygtPage.intygStatus.getText()).to.eventually.contain(statustext).and.notify(callback);
    });

    this.Given(/^ska statusuppdatering "([^"]*)" skickas till vårdsystemet\. Totalt: "([^"]*)"$/, function(handelsekod, antal, callback) {
        waitForCount(global.intyg.id, handelsekod, parseInt(antal, 10), callback);
    });

    this.Given(/^ska (\d+) statusuppdatering "([^"]*)" skickas för det ursprungliga intyget$/, function(antal, handelsekod, callback) {
        waitForCount(ursprungligtIntyg.id, handelsekod, parseInt(antal, 10), callback);
    });

    this.Given(/^jag raderar intyget$/, function(callback) {
        fk7263Utkast.radera.knapp.sendKeys(protractor.Key.SPACE).then(function() {
            fk7263Utkast.radera.bekrafta.sendKeys(protractor.Key.SPACE).then(callback);
        });
    });


    this.Given(/^ska statusuppdateringen visa frågor (\d+), hanterade frågor (\d+),antal svar (\d+), hanterade svar (\d+)$/, function(fragor, hanFragor, svar, hanSvar) {
        console.log(statusuppdateringarRows[0]);
        var row = statusuppdateringarRows[0];
        expect(fragor).to.equal(row.antalFragor.toString());
        expect(hanFragor).to.equal(row.antalHanteradeFragor.toString());
        expect(svar).to.equal(row.antalSvar.toString());
        expect(hanSvar).to.equal(row.antalHanteradeSvar.toString());
    });
};