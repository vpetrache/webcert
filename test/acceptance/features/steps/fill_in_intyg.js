/*global
testdata, intyg, browser, pages*/
'use strict';

var tsdUtkastPage = pages.intygpages['ts-diabetesUtkast'];
var tsBasUtkastPage = pages.intygpages['ts-basUtkast'];
var fkUtkastPage = pages.intygpages.fk7263Utkast;

module.exports = function() {

    this.Given(/^jag fyller i alla nödvändiga fält för intyget$/, function(callback) {
        if (intyg.typ === 'Transportstyrelsens läkarintyg') {
            global.intyg = testdata.getRandomTsBasIntyg();

            // browser.ignoreSynchronization = true;
            // Intyget avser
            tsBasUtkastPage.fillInKorkortstyper(global.intyg.korkortstyper, 'intygetAvserForm');
            // Identiteten är styrkt genom
            tsBasUtkastPage.fillInIdentitetStyrktGenom(intyg.identitetStyrktGenom);

            // Synfunktioner

            browser.ignoreSynchronization = true;
            tsBasUtkastPage.fillInSynfunktioner(global.intyg);
            tsBasUtkastPage.fillInHorselOchBalanssinne(global.intyg);
            tsBasUtkastPage.fillInRorelseorganensFunktioner(global.intyg);
            tsBasUtkastPage.fillInHjartOchKarlsjukdomar(global.intyg);
            tsBasUtkastPage.fillInDiabetes(global.intyg);
            tsBasUtkastPage.fillInHorselOchBalanssinne(global.intyg);
            tsBasUtkastPage.fillInNeurologiskaSjukdomar(global.intyg);
            tsBasUtkastPage.fillInEpilepsi(global.intyg);
            tsBasUtkastPage.fillInNjursjukdomar(global.intyg);
            tsBasUtkastPage.fillInDemens(global.intyg);
            tsBasUtkastPage.fillInSomnOchVakenhet(global.intyg);
            tsBasUtkastPage.fillInAlkoholNarkotikaLakemedel(global.intyg);
            tsBasUtkastPage.fillInPsykiska(global.intyg);
            tsBasUtkastPage.fillInAdhd(global.intyg);
            tsBasUtkastPage.fillInSjukhusvard(global.intyg);

            tsBasUtkastPage.fillInOvrigMedicinering(global.intyg);

            browser.ignoreSynchronization = false;
            tsBasUtkastPage.fillInBedomning(intyg.bedomning);
            callback();
        } else if (intyg.typ === 'Transportstyrelsens läkarintyg, diabetes') {
            global.intyg = testdata.getRandomTsDiabetesIntyg();

            tsdUtkastPage.fillInKorkortstyper(intyg.korkortstyper);
            tsdUtkastPage.fillInIdentitetStyrktGenom(intyg.identitetStyrktGenom);
            tsdUtkastPage.fillInAllmant(intyg.allmant);
            tsdUtkastPage.fillInHypoglykemier(intyg.hypoglykemier);
            tsdUtkastPage.fillInSynintyg(intyg.synintyg);
            tsdUtkastPage.fillInBedomning(intyg.bedomning);

            callback();
        } else if (intyg.typ === 'Läkarintyg FK 7263') {

            global.intyg = testdata.fk.sjukintyg.getRandom();
            fkUtkastPage.angeSmittskydd(intyg.smittskydd);
            fkUtkastPage.angeIntygetBaserasPa(intyg.baserasPa);
            fkUtkastPage.angeDiagnoser(intyg.diagnos);
            fkUtkastPage.angeAktuelltSjukdomsForlopp(intyg.aktuelltSjukdomsforlopp);
            fkUtkastPage.angeArbetsformaga(intyg.arbetsformaga);
            fkUtkastPage.angeArbetsformagaFMB(intyg.arbetsformagaFMB);
            fkUtkastPage.angePrognos(intyg.prognos);
            callback();
        }
    });

    this.Given(/^jag jämför att all data är rätt$/, function(callback) {
        expect(intyg).to.equal(global.intyg);
        callback();
    });


};