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

/*globals describe,it,browser */
'use strict';
var wcTestTools = require('webcert-testtools');
var specHelper = wcTestTools.helpers.spec;
var testdataHelper = wcTestTools.helpers.restTestdata;
var UtkastPage = wcTestTools.pages.intyg.luaeFS.utkast;
var IntygPage = wcTestTools.pages.intyg.luaeFS.intyg;

xdescribe('luaefs.utkast.create - Create and Sign luae_fs utkast', function() {

    var utkastId = null;

    beforeAll(function() {
        browser.ignoreSynchronization = false;
        specHelper.login();
        specHelper.createUtkastForPatient('191212121212',
            'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång');
    });

    describe('Fyll i luae_fs intyg', function() {

        it('Spara undan intygsId från URL', function() {

            // Save id so it can be removed in cleanup stage.
            browser.getCurrentUrl().then(function(url) {
                utkastId = url.split('/').pop();
            });
        });

        it('tomt utkast skall visa lista med fel efter klick på Signera', function() {

            UtkastPage.disableAutosave();

            UtkastPage.signeraButtonClick();

            expect(UtkastPage.getMissingInfoMessagesCount()).toBe(3);

        });

        it('Grund - baserat på', function() {

            var promiseArr = [];

            promiseArr.push(UtkastPage.angeIntygetBaserasPa({
                minUndersokningAvPatienten: {
                    datum: '2016-04-22'
                }
            }));

            promiseArr.push(UtkastPage.angeIntygetBaserasPa({
                kannedomOmPatient: {
                    datum: '2016-04-21'
                }
            }));

            promiseArr.push(UtkastPage.angeIntygetBaserasPa({
                annat: {
                    datum: '2016-04-23',
                    beskrivning: 'Utlåtande från skolledningen'
                }
            }));

            Promise.all(promiseArr);

            expect(UtkastPage.baseratPa.minUndersokningAvPatienten.datum.getAttribute('value')).toBe('2016-04-22');
            expect(UtkastPage.baseratPa.kannedomOmPatient.datum.getAttribute('value')).toBe('2016-04-21');
            expect(UtkastPage.baseratPa.annat.datum.getAttribute('value')).toBe('2016-04-23');
            expect(UtkastPage.baseratPa.annat.beskrivning.getAttribute('value')).toBe(
                'Utlåtande från skolledningen');
        });

        it('Andra medicinska utredningar eller underlag', function() {
            var utredningar = [{
                underlag: 'Neuropsykiatriskt utlåtande',
                datum: '2016-04-16',
                infoOmUtredningen: 'Hämtas hos posten'
            }];
            UtkastPage.angeAndraMedicinskaUtredningar(utredningar);
        });

        it('Ange diagnoser', function() {
            var diagnosObj = {
                diagnoser: [{
                    'kod': 'J21'
                }, {
                    'kod': 'J22'
                }, {
                    'kod': 'A21'
                }]
            };
            UtkastPage.angeDiagnos(diagnosObj);
        });

        it('Ange funktionsnedsättningar', function() {
            UtkastPage.funktionsnedsattning.debut.sendKeys('Komplex tango på skoldansen, knäfraktur.');
            UtkastPage.funktionsnedsattning.paverkan.sendKeys(
                'Dansen funkar inte längre, svårt att fullfölja baletten.');
        });

        it('Ange övrigt', function() {
            UtkastPage.ovrigt.sendKeys('Behöver nog ett par år extra för att komma ikapp efter skadan.');
        });

        it('Ange kontakt önskas', function() {
            UtkastPage.kontaktMedFkNo.sendKeys(protractor.Key.SPACE).then(function(){
                UtkastPage.anledningTillKontakt.sendKeys('Patienten känner att en avstämning vore bra.');
            });
        });
/* no tilläggsfrågor atm. activate if we get such in the future
        it('Ange tilläggsfrågor', function() {
            UtkastPage.tillaggsfragor0svar.sendKeys('Vad för slags fråga är det där?!?!?');
            UtkastPage.enableAutosave();
            UtkastPage.tillaggsfragor1svar.sendKeys(
                'Likheten på en struts? Båda benen är lika långa, särskilt det vänstra.');
        });
*/
        it('Signera intyget', function() {
            UtkastPage.whenSigneraButtonIsEnabled().then(function() {
                UtkastPage.signeraButtonClick();
                expect(IntygPage.isAt()).toBeTruthy();
            });
        });
    });

    afterAll(function() {
        testdataHelper.deleteIntyg(utkastId);
        testdataHelper.deleteUtkast(utkastId);
        specHelper.logout();
    });

});
