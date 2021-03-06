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

/*globals element,by, Promise, protractor, browser*/
'use strict';

var FkBaseUtkast = require('../fk.base.utkast.page.js');

function sendKeysWithBackspaceFix(el, text) {
    return el.sendKeys(text)
        .then(function() {
            return el.sendKeys(protractor.Key.BACK_SPACE);
        })
        .then(function() {
            return el.sendKeys(text.substr(text.length - 1));
        });
}

function sendEnterToElement(el) {
    return function() {
        el.sendKeys(protractor.Key.ENTER);
    };
}

var LuaefsUtkast = FkBaseUtkast._extend({
    init: function init() {
        init._super.call(this);

        this.at = element(by.css('.edit-form'));

        this.andraMedicinskaUtredningar = {
            finns: {
                JA: element(by.id('underlagFinnsYes')),
                NEJ: element(by.id('underlagFinnsNo'))
            },
            underlagRow: function(index) {
                return {
                    underlag: element(by.id('underlag-' + index + '-typ')),
                    datum: element(by.id('underlag-' + index + '-datum')),
                    information: element(by.id('underlag-' + index + '-hamtasFran'))
                };
            }
        };

        this.diagnos = {
            diagnosRow: function(index) {
                return {
                    kod: element(by.id('diagnoseCode-' + index))
                };
            }
        };

        this.funktionsnedsattning = {
            debut: element(by.id('funktionsnedsattningDebut')),
            paverkan: element(by.id('funktionsnedsattningPaverkan'))
        };

        this.ovrigt = element(by.id('ovrigt'));

        this.kontaktMedFK = element(by.id('form_kontaktMedFk')).element(by.css('input'));
        this.kontaktMedFkNo = element(by.id('formly_1_checkbox-inline_kontaktMedFk_0'));
        this.anledningTillKontakt = element(by.id('anledningTillKontakt'));

        this.tillaggsfragor0svar = element(by.id('tillaggsfragor[0].svar'));
        this.tillaggsfragor1svar = element(by.id('tillaggsfragor[1].svar'));

        this.enhetensAdress = {
            postAdress: element(by.id('grundData.skapadAv.vardenhet.postadress')),
            postNummer: element(by.id('grundData.skapadAv.vardenhet.postnummer')),
            postOrt: element(by.id('grundData.skapadAv.vardenhet.postort')),
            enhetsTelefon: element(by.id('grundData.skapadAv.vardenhet.telefonnummer'))
        };

        this.baseratPa = {
            minUndersokningAvPatienten: {
                checkbox: element(by.id('formly_1_date_undersokningAvPatienten_3')),
                datum: element(by.id('form_undersokningAvPatienten')).element(by.css('input[type=text]'))
            },
            journaluppgifter: {
                checkbox: element(by.id('formly_1_date_journaluppgifter_4')),
                datum: element(by.id('form_journaluppgifter')).element(by.css('input[type=text]'))
            },
            anhorigBeskrivning: {
                checkbox: element(by.id('form_anhorigsBeskrivningAvPatienten')),
                datum: element(by.id('form_anhorigsBeskrivningAvPatienten')).element(by.css('input[type=text]'))
            },
            annat: {
                beskrivning: element(by.id('annatGrundForMUBeskrivning')),
                checkbox: element(by.id('formly_1_date_annatGrundForMU_6')),
                datum: element(by.id('form_annatGrundForMU')).all(by.css('input[type=text]')).first()
            },
            kannedomOmPatient: {
                datum: element(by.id('form_kannedomOmPatient')).element(by.css('input[type=text]')),
                checkbox: element(by.id('formly_1_date_kannedomOmPatient_8'))
            }
        };
        this.kontaktMedFK = element(by.id('form_kontaktMedFk')).element(by.css('input'));
        this.anledningTillKontakt = element(by.id('anledningTillKontakt'));
    },
    angeBaseratPa: function(baseratPa) {
        var promiseArr = [];
        if (baseratPa.minUndersokningAvPatienten) {
            promiseArr.push(sendKeysWithBackspaceFix(this.baseratPa.minUndersokningAvPatienten.datum, baseratPa.minUndersokningAvPatienten));
        }
        if (baseratPa.journaluppgifter) {
            promiseArr.push(sendKeysWithBackspaceFix(this.baseratPa.journaluppgifter.datum, baseratPa.journaluppgifter));

        }
        if (baseratPa.anhorigsBeskrivning) {
            promiseArr.push(sendKeysWithBackspaceFix(this.baseratPa.anhorigBeskrivning.datum, baseratPa.anhorigsBeskrivning));

        }

        if (baseratPa.annat) {
            var annatEl = this.baseratPa.annat;
            promiseArr.push(
                sendKeysWithBackspaceFix(annatEl.datum, baseratPa.annat)
                .then(function() {
                    return annatEl.beskrivning.sendKeys(baseratPa.annatBeskrivning);
                })
            );
        }

        if (baseratPa.personligKannedom) {
            promiseArr.push(sendKeysWithBackspaceFix(this.baseratPa.kannedomOmPatient.datum, baseratPa.personligKannedom));
        }

        return Promise.all(promiseArr);
    },

    angeDiagnosKoder: function(diagnoser) {
        var promiseArr = [];
        for (var i = 0; i < diagnoser.length; i++) {
            var row = this.diagnos.diagnosRow(i);
            promiseArr.push(row.kod.sendKeys(diagnoser[i].kod).then(sendEnterToElement(row.kod)));

        }
        return Promise.all(promiseArr);

    },
    angeDiagnos: function(diagnosObj) {
        var diagnoser = diagnosObj.diagnoser;
        var promiseArr = [];


        //Ange diagnoser
        promiseArr.push(this.angeDiagnosKoder(diagnoser));

        return Promise.all(promiseArr);
    },

    angeAndraMedicinskaUtredningar: function(utredningar) {
        var utredningarElement = this.andraMedicinskaUtredningar;

        var fillIn = function fillInUtr(val, index) {

            var row = utredningarElement.underlagRow(index);

            return Promise.all([
                sendKeysWithBackspaceFix(row.datum, val.datum),
                row.underlag.click(),
                row.underlag.element(by.cssContainingText('div', val.underlag)).click(),
                row.information.sendKeys(val.infoOmUtredningen)
            ]);
        };

        if (utredningar) {
            return utredningarElement.finns.JA.sendKeys(protractor.Key.SPACE)
                .then(function() {
                    browser.sleep(2000);
                    var actions = utredningar.map(fillIn);
                    return actions;
                });
        } else {
            return utredningarElement.finns.NEJ.sendKeys(protractor.Key.SPACE);
        }
    },
    angeFunktionsnedsattning: function(funktionsnedsattning) {
        var fn = this.funktionsnedsattning;

        var promisesArr = [];
        promisesArr.push(fn.debut.sendKeys(funktionsnedsattning.debut));
        promisesArr.push(fn.paverkan.sendKeys(funktionsnedsattning.paverkan));

        return Promise.all(promisesArr);
    },

    angeUnderlagFinns: function(underlag) {
        if (!underlag) {
            return Promise.resolve('Success');
        }

        var promisesArr = [];
        promisesArr.push(this.underlagDatePicker1.sendKeys(underlag.datum));
        promisesArr.push(this.underlagSelect1.sendKeys(underlag.typ));
        promisesArr.push(this.underlagTextField1.sendKeys(underlag.hamtasFran));

        Promise.all(promisesArr);
    },

    angeIntygetBaserasPa: function(intygetBaserasPa) {
        if (!intygetBaserasPa) {
            return Promise.resolve('Success');
        }

        var promisesArr = [];

        if (intygetBaserasPa.minUndersokningAvPatienten) {
            promisesArr.push(this.baseratPa.minUndersokningAvPatienten.datum.sendKeys(intygetBaserasPa.minUndersokningAvPatienten.datum));
        }
        if (intygetBaserasPa.journaluppgifter) {
            promisesArr.push(this.baseratPa.journaluppgifter.datum.sendKeys(intygetBaserasPa.journaluppgifter.datum));
        }
        if (intygetBaserasPa.anhorigBeskrivning) {
            promisesArr.push(this.baseratPa.anhorigBeskrivning.datum.sendKeys(intygetBaserasPa.anhorigBeskrivning.datum));
        }
        if (intygetBaserasPa.annat) {
            promisesArr.push(this.baseratPa.annat.datum.sendKeys(intygetBaserasPa.annat.datum));
            promisesArr.push(this.baseratPa.annat.beskrivning.sendKeys(intygetBaserasPa.annat.beskrivning));
        }
        if (intygetBaserasPa.kannedomOmPatient) {
            promisesArr.push(this.baseratPa.kannedomOmPatient.datum.sendKeys(intygetBaserasPa.kannedomOmPatient.datum));
        }
        return Promise.all(promisesArr);
    },

    getNumberOfDiagnosRows: function() {
        return element.all(by.css('.diagnosRow')).then(function(items) {
            return items.length;
        });
    },
    angeOvrigaUpplysningar: function(ovrigt) {
        return this.ovrigt.sendKeys(ovrigt);
    },

    getNumberOfUnderlag: function() {
        return element.all(by.css('.underlagRow td select')).then(function(items) {
            return items.length;
        });
    },
    angeKontaktMedFK: function(kontakt) {
        if (kontakt) {
            return this.kontaktMedFK.sendKeys(protractor.Key.SPACE);
        } else {
            return Promise.resolve();
        }
    },

    get: function get(intygId) {
        get._super.call(this, 'luae_fs', intygId);
    },
    isAt: function isAt() {
        return isAt._super.call(this);
    }
});

module.exports = new LuaefsUtkast();
