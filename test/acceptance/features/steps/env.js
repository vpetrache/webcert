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

/* globals browser, logger, JSON */
'use strict';

var hasFoundConsoleErrors = false;
var fs = require('fs');

function writeScreenShot(data, filename, cb) {
    var stream = fs.createWriteStream(filename);
    stream.write(new Buffer(data, 'base64'));
    stream.end();
    stream.on('finish', cb);
}

function checkConsoleErrors(cb) {
    if (hasFoundConsoleErrors) {
        logger.info(hasFoundConsoleErrors);

        // 500-error är ett godkänt fel i detta test, se INTYG-3524
        if (global.scenario.getName().indexOf('Kan byta vårdenhet') >= 0 && hasFoundConsoleErrors.indexOf('error 500') > -1) {
            logger.info('Hittade 500-fel. Detta fel är accepterat, se INTYG-3524');
        } else {
            throw ('Hittade script-fel under körning');
        }
    }
    cb();
}

module.exports = function() {
    this.setDefaultTimeout(400 * 1000);
    global.externalPageLinks = [];

    this.AfterStep(function(event, callback) {

        // Samla in alla externa länkar på aktuell sida
        element.all(by.css('a')).each(function(link) {
            link.getAttribute('href').then(function(href) {
                if (href !== null &&
                    href !== '' &&
                    href.includes('javascript') !== true &&
                    href.indexOf(process.env.WEBCERT_URL) === -1 &&
                    href.indexOf(process.env.MINAINTYG_URL) === -1 &&
                    href.indexOf(process.env.REHABSTOD_URL) === -1 &&
                    global.externalPageLinks.indexOf(href) === -1) {
                    console.log('Found one: ' + href);
                    global.externalPageLinks.push(href);
                }
            });
        }).then(function() {

            //Skriv ut script-fel, Kan inte kasta fel i AfterStep tyvärr
            browser.executeScript('return window.errs;').then(function(v) {
                if (v && v.length > 0) {
                    hasFoundConsoleErrors = JSON.stringify(v);
                    logger.info(hasFoundConsoleErrors);

                }
                callback();
            });

        });

    });

    this.Before(function(scenario) {
        global.scenario = scenario;

        //Återställ globala variabler
        global.person = {};
        global.intyg = {};
        global.meddelanden = []; //{typ:'', id:''}
        global.user = {};
        hasFoundConsoleErrors = false;
    });
    //After scenario
    this.After(function(scenario, callback) {

        console.log('Rensar local-storage');
        browser.executeScript('window.sessionStorage.clear();');
        browser.executeScript('window.localStorage.clear();');

        //Ska intyg rensas bort efter scenario?
        var rensaBortIntyg = true;
        var tagArr = scenario.getTags();
        for (var i = 0; i < tagArr.length; i++) {
            if (tagArr[i].getName() === '@keepIntyg') {
                rensaBortIntyg = false;
            }
        }

        if (scenario.isFailed()) {
            browser.takeScreenshot().then(function(png) {
                var ssPath = './node_modules/common-testtools/cucumber-html-report/';
                var filename = 'screenshots/' + new Date().getTime() + '.png';
                writeScreenShot(png, ssPath + filename, function() {
                    scenario.attach(filename, 'image/png', function(err) {
                        if (err) {
                            throw err;
                        }
                        checkConsoleErrors(callback);
                    });
                });
            });
        } else {
            checkConsoleErrors(callback);
        }


    });



    logger.on('logging', function(transport, level, msg, meta) {
        if (global.scenario) {
            global.scenario.attach(level + ': ' + msg);
        }
    });



};
