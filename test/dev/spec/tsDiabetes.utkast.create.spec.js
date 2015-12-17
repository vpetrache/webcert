/**
 * Created by bennysce on 09/06/15.
 */
/*globals describe,it */
'use strict';

var specHelper = wcTestTools.helpers.spec;
var testdataHelper = wcTestTools.helpers.testdata;
var tsdUtkastPage = wcTestTools.pages.intygpages['ts-diabetesUtkast'];
var tsdIntygPage = wcTestTools.pages.intygpages.tsDiabetesIntyg;

describe('Create and Sign ts-diabetes utkast', function() {

    var utkast = null;

    describe('prepare test with intyg', function() {
        it('should generate ts-diabetes min intyg', function() {
            testdataHelper.createUtkast('ts-diabetes').then(function(response) {
                utkast = response.body;
                expect(utkast.intygsId).not.toBeNull();
            }, function(error) {
                console.log('Error calling createIntyg');
            });
        });
    });

    describe('User', function() {
        it('should login and open utkast', function() {
            browser.ignoreSynchronization = false;
            specHelper.login();
            tsdUtkastPage.get(utkast.intygsId);
        });
    });

    describe('fill utkast', function() {

        it('should be able to sign utkast', function() {

            browser.ignoreSynchronization = true;

            // Intyget avser
            tsdUtkastPage.fillInKorkortstyper(['D']);

            // Identiteten är styrkt genom
            tsdUtkastPage.fillInIdentitetStyrktGenom(wcTestTools.utkastTextmap.ts.identitetStyrktGenom.pass);
        });

        it('allmant', function() {

            var allmant = {
                year: '2015',
                typ: wcTestTools.utkastTextmap.ts.diabetes.typ.typ1,
                behandling: {
                    typer: [wcTestTools.utkastTextmap.ts.diabetes.behandling.endastkost,
                        wcTestTools.utkastTextmap.ts.diabetes.behandling.insulin],
                    insulinYear: '2000'
                }
            };

            // Allmänt
            tsdUtkastPage.fillInAllmant(allmant);
        });

        it('hypo', function() {

            // Hypoglykemier
            tsdUtkastPage.fillInHypoglykemier({
                a: 'Ja',
                b: 'Nej',
                f: 'Ja',
                g: 'Nej'
            });

            //Synintyg
            tsdUtkastPage.fillInSynintyg({ a: 'Ja' });

            //Bedömning
            var bedomning = {
                stallningstagande: 'behorighet_bedomning',
                behorigheter: ['D'],
                lamplighet: 'Ja'
            };

            tsdUtkastPage.fillInBedomning(bedomning);
        });

        it('should be able to sign utkast', function() {
            browser.ignoreSynchronization = false;
            tsdUtkastPage.whenSigneraButtonIsEnabled().then(function() {
                tsdUtkastPage.signeraButtonClick();
                expect(tsdIntygPage.isAt()).toBeTruthy();
            });
        });

        describe('remove test intyg', function() {
            it('should clean up all utkast after the test', function() {
                testdataHelper.deleteUtkast(utkast.intygsId);
                testdataHelper.deleteIntyg(utkast.intygsId);
            });
        });
    });
});