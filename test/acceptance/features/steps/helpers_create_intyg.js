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

/*global intyg,logger,pages,Promise,wcTestTools,user,person,protractor*/
'use strict';
var testdataHelper = wcTestTools.helpers.testdata;
var loginHelpers = require('./inloggning/login.helpers.js');
// var restTestdataHelper = wcTestTools.helpers.restTestdata;
var sokSkrivIntygPage = pages.sokSkrivIntyg.pickPatient;
var sokSkrivIntygUtkastTypePage = pages.sokSkrivIntyg.valjUtkastType;
var fkUtkastPage = pages.intyg.fk['7263'].utkast;
var fkIntygPage = pages.intyg.fk['7263'].intyg;
var rUtil = wcTestTools.restUtil;
var intygGenerator = require('../../../webcertTestTools/util/intygGenerator.util.js');

function createIntygWithRest(intygOptions, cb) {
    var userObj = {
        fornamn: user.fornamn,
        efternamn: user.efternamn,
        hsaId: user.hsaId,
        enhetId: user.enhetId,
        lakare: user.lakare,
        forskrivarKod: user.forskrivarKod
    };

    rUtil.login(userObj).then(function(data) {
        logger.debug('Login OK');
        return Promise.resolve('SUCCESS');
    }, function(error) {
        cb(error);
    }).then(function() {
        rUtil.createIntyg(intygGenerator.buildIntyg(intygOptions)).then(function(response) {
            logger.info('Skapat intyg via REST-api');
            cb();
        }, function(error) {
            cb(error);
        });
    });
}


function createTsIntyg(typ, status, cb) {
    var standardUser = global.user;

    var userObj = {
        fornamn: 'Erik',
        efternamn: 'Nilsson',
        hsaId: 'TSTNMT2321000156-105H',
        enhetId: standardUser.enhetId,
        lakare: true
    };
    loginHelpers.logInAsUserRole(userObj, 'Läkare')
        .and.notify(function() {
            sokSkrivIntygPage.selectPersonnummer(person.id);
            sokSkrivIntygUtkastTypePage.selectIntygTypeByLabel(typ);
            sokSkrivIntygUtkastTypePage.intygTypeButton.sendKeys(protractor.Key.SPACE);
            global.intyg = require('./helpers').generateIntygByType(typ);
            require('./fillIn').fillIn(intyg, function() {
                fkUtkastPage.signeraButton.sendKeys(protractor.Key.SPACE);
                if (status === 'Mottaget') {
                    fkIntygPage.skicka.knapp.sendKeys(protractor.Key.SPACE);
                    fkIntygPage.skicka.samtyckeCheckbox.sendKeys(protractor.Key.SPACE);
                    fkIntygPage.skicka.dialogKnapp.sendKeys(protractor.Key.SPACE);
                }

                var userObj = {
                    fornamn: standardUser.fornamn,
                    efternamn: standardUser.efternamn,
                    hsaId: standardUser.hsaId,
                    enhetId: standardUser.enhetId,
                    lakare: standardUser.lakare,
                    origin: standardUser.origin
                };

                loginHelpers.logInAsUserRole(userObj, standardUser.roleName).and.notify(cb);
            });
        });
}

module.exports = {
    createIntygWithStatus: function(typ, status, cb) {
        //TODO, Hantera ts-intyg

        intyg.id = testdataHelper.generateTestGuid();
        logger.debug('intyg.id = ' + intyg.id);

        if (typ.indexOf('Transportstyrelsen') > -1) {
            createTsIntyg(typ, status, cb);

        } else if (typ === 'Läkarintyg FK 7263') {

            createIntygWithRest({
                personnr: person.id,
                patientNamn: 'Test Testsson',
                //issuerId : '',
                issuer: user.fornamn + ' ' + user.efternamn,
                issued: '2015-04-01',
                validFrom: '2015-04-01',
                validTo: '2015-04-11',
                enhetId: user.enhetId,
                //enhet : '',
                vardgivarId: 'TSTNMT2321000156-1002',
                intygType: 'fk7263',
                intygId: intyg.id,
                sent: (status === 'Mottaget' || status === 'Makulerat'),
                revoked: (status === 'Makulerat')
            }, cb);
        } else if (typ === 'Läkarutlåtande för sjukersättning') {

            createIntygWithRest({
                personnr: person.id,
                patientNamn: 'Test Testsson',
                issuer: user.hsaId,
                issued: '2016-04-01',
                validFrom: '2016-04-01',
                validTo: '2016-04-11',
                enhetId: user.enhetId,
                vardgivarId: 'TSTNMT2321000156-1002',
                intygType: 'luse',
                intygId: intyg.id,
                sent: (status === 'Mottaget' || status === 'Makulerat'),
                revoked: (status === 'Makulerat')
            }, cb);

        } else {
            cb('TODO: Hantera fall då det inte redan finns något intyg att använda');
        }
    }
};
