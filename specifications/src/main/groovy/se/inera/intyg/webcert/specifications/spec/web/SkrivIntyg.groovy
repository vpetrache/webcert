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

package se.inera.intyg.webcert.specifications.spec.web
import org.openqa.selenium.Keys
import se.inera.intyg.webcert.specifications.page.AbstractPage
import se.inera.intyg.webcert.specifications.spec.Browser
import se.inera.intyg.webcert.specifications.pages.AbstractEditCertPage
import se.inera.intyg.webcert.specifications.pages.SokSkrivValjIntygTypPage
import se.inera.intyg.webcert.specifications.pages.SokSkrivaIntygPage
import se.inera.intyg.webcert.specifications.pages.fk7263.EditeraFk7263Page
import se.inera.intyg.webcert.specifications.pages.ts_bas.EditeraTsBasPage
import se.inera.intyg.webcert.specifications.pages.ts_diabetes.EditeraTsDiabetesPage
import se.inera.intyg.webcert.specifications.spec.util.screenshot.ExceptionHandlingFixture

class SkrivIntyg extends ExceptionHandlingFixture {

    def intygsid

    def skapaNyttIntygsutkastForPatientAvTyp(String patient, String typ) {
        Browser.drive {
            waitFor {
                at SokSkrivaIntygPage
            }
            page.angePatient(patient)

            waitFor {
                at SokSkrivValjIntygTypPage
            }
            page.valjIntygsTyp(typ)

            waitForIntygstyp(typ)

            intygsid = currentUrl.substring(currentUrl.lastIndexOf("/") + 1)
        }
    }

    void waitForIntygstyp(String intygstyp) {
        String typ = intygstyp.toLowerCase()
        def page = AbstractEditCertPage

        if (typ == "fk7263") {
            page = EditeraFk7263Page
        } else if (typ == "ts-bas") {
            page = EditeraTsBasPage
        } else if (typ == "ts-diabetes") {
            page = EditeraTsDiabetesPage
        }

        Browser.drive {
            waitFor {
                at page
            }
        }
    }

    String intygsid() {
        intygsid
    }

    boolean visaVadSomSaknasListaVisas() {
        boolean result
        Browser.drive {
            result = page.visaVadSomSaknasLista.isDisplayed()
        }
        return result
    }

    boolean ingaValideringsfelVisas() {
        def result
        Browser.drive {
            result = !page.valideringIntygBaseratPa.isDisplayed() &&
                    !page.valideringDiagnos.isDisplayed() &&
                    !page.valideringFunktionsnedsattning.isDisplayed() &&
                    !page.valideringAktivitetsbegransning.isDisplayed() &&
                    !page.valideringSysselsattning.isDisplayed() &&
                    !page.valideringArbetsformaga.isDisplayed() &&
                    !page.valideringPrognos.isDisplayed() &&
                    !page.valideringRekommendationer.isDisplayed() &&
                    !page.valideringVardperson.isDisplayed()
        }
        result
    }

    // --- validering

    boolean valideringsfelIntygBaseratPaVisas() {
        def result
        Browser.drive {
            result = page.valideringIntygBaseratPa?.isDisplayed()
        }
        result
    }

    boolean valideringsfelDiagnosVisas() {
        def result
        Browser.drive {
            result = page.valideringDiagnos?.isDisplayed()
        }
        result
    }

    boolean valideringsfelFunktionsnedsattningVisas() {
        def result
        Browser.drive {
            result = page.valideringFunktionsnedsattning.isDisplayed()
        }
        result
    }

    boolean valideringsfelAktivitetsbegransningVisas() {
        def result
        Browser.drive {
            result = page.valideringAktivitetsbegransning?.isDisplayed()
        }
        result
    }

    boolean valideringsfelSysselsattningVisas() {
        def result
        Browser.drive {
            result = page.valideringSysselsattning?.isDisplayed();
        }
        result
    }

    boolean valideringsfelArbetsformagaVisas() {
        def result
        Browser.drive {
            result = page.valideringArbetsformaga?.isDisplayed()
        }
        result
    }

    boolean valideringsfelPrognosVisas() {
        def result
        Browser.drive {
            result = page.valideringPrognos?.isDisplayed()
        }
        result
    }

    boolean valideringsfelRekommendationerVisas() {
        def result
        Browser.drive {
            result = page.valideringRekommendationer?.isDisplayed()
        }
        result
    }

    boolean valideringsfelVardpersonVisas() {
        def result
        Browser.drive {
            result = page.valideringVardperson?.isDisplayed()
        }
        result
    }

    // ------- validering end

    /**
     * Simulate entering of a diagnosis code on athe given diagnos input field
     *
     * @param diagnosCode
     * @param diagnosField
     * @return
     */
    def mataInDiagnoskodFörDiagnosfältMedId(String diagnosCode, String diagnosField) {

        Browser.drive {
            AbstractPage.scrollIntoView("diagnoseCode");
            //definera elementreferens till det type-ahead element vi vill välja
            def diagnoseCodeElement = page.$('#' + diagnosField)

            waitFor {
                diagnoseCodeElement.isDisplayed()
            }

            //..och sätt värdet
            diagnoseCodeElement << diagnosCode
        }
    }

    /**
     * Simulate selecting a type-ahead suggestion fo a more detailed diagnosis code in the suggestion dropdown attached
     * to a given diagnos code/description field.
     *
     * @param diagnosField
     * @param diagnosForslagsTextAttValja
     * @return
     */
    def väljDiagnosförslagFörFältSomInnehållerText(String diagnosField, String diagnosForslagsTextAttValja) {
        Browser.drive {
            AbstractPage.scrollIntoView("diagnoseCode");
            //Definera elementreferens till det type-ahead element vi vill välja
            def typeAheadElement = page.$('#' + diagnosField + ' + UL li a', text: contains(diagnosForslagsTextAttValja))[0]

            //..vänta på att ett sådant förslag är synligt...
            waitFor {
                typeAheadElement.isDisplayed()
            }

            //..och klicka på det
            typeAheadElement.click()
        }
    }

    def vanta(int sekunder) {
        Browser.drive {
            Thread.sleep(sekunder * 1000)
        }
        true
    }

    boolean meddelandeIntygetKomplettVisas() {
        boolean result
        Browser.drive {
            waitFor {
                page.intygetSparatOchKomplettMeddelande.isDisplayed()
            }
            result = page.intygetSparatOchKomplettMeddelande?.isDisplayed()
        }
        result
    }

    boolean meddelandeIntygetEjKomplettVisas() {
        boolean result
        Browser.drive {
            waitFor {
                page.intygetSparatOchEjKomplettMeddelande.isDisplayed()
            }
            result = page.intygetSparatOchEjKomplettMeddelande?.isDisplayed()
        }
        result
    }

    String sjukskrivningsperiod() {
        def result
        Browser.drive {
            waitFor {
                at EditeraFk7263Page
            }
            result = page.arbetsformaga.period.text()
        }
        return result
    }

    String arbetstid25() {
        String result
        Browser.drive {
            waitFor {
                at EditeraFk7263Page
            }
            result = page.arbetsformaga.arbetstid25.text()
        }
        result
    }

    String arbetstid50() {
        String result
        Browser.drive {
            waitFor {
                at EditeraFk7263Page
            }
            result = page.arbetsformaga.arbetstid50.text()
        }
        result
    }

    String arbetstid75() {
        String result
        Browser.drive {
            waitFor {
                at EditeraFk7263Page
            }
            result = page.arbetsformaga.arbetstid75.text()
        }
        result
    }

    String diagnos1Kod() {
        def result
        Browser.drive {
            result = page.diagnos.diagnos1.value()
        }
        result
    }



    String diagnos2Kod() {
        def result
        Browser.drive {
            result = page.diagnos.diagnos2.value()
        }
        result
    }

    String diagnos3Kod() {
        def result
        Browser.drive {
            result = page.diagnos.diagnos3.value()
        }
        result
    }

    String diagnos1Text() {
        def result
        Browser.drive {
            result = page.diagnos.diagnos1Text.value()
        }
        result
    }

    String diagnos2Text() {
        def result
        Browser.drive {
            result = page.diagnos.diagnos2Text.value()
        }
        result
    }

    String diagnos3Text() {
        def result
        Browser.drive {
            result = page.diagnos.diagnos3Text.value()
        }
        result
    }

    void enterPaDiagnosKod() {
        Browser.drive {
            page.diagnos.diagnos1 << Keys.ENTER
            waitFor {
                page.doneLoading()
            }
        }
    }

    void oppnaDatePicker() {
        Browser.drive {
            baserasPa.undersokningDatumToggle();
            waitFor {
                page.doneLoading()
            }
        }
    }

    boolean datePickerVisas() {
        def result
        Browser.drive {
            waitFor {
                page.doneLoading()
            }
            result = page.datepicker.isDisplayed()
        }
        result
    }

    String prognos() {
        def result = '';
        Browser.drive {
            result = page.prognos.prognosValue();
        }
        result
    }

    boolean prognosArVald() {
        def result;
        Browser.drive {
            result = page.prognos.prognos.value() != null;
        }
        result
    }

    String ressatt() {
        def result = ''
        Browser.drive {
            result = page.rekommendationer.radioGroupResor
        }
        result
    }

    String rehab() {
        def result = ''
        Browser.drive {
            result = page.rekommendationer.radioGroupRehab
        }
        result
    }

    String ressattNej() {
        def result = ''
        Browser.drive {
            result = page.rekommendationer.ressattNej.value();
        }
        result
    }

    String ressattJa() {
        def result = ''
        Browser.drive {
            result = page.rekommendationer.ressattJa.value();
        }
        result
    }

    String rehabJa() {
        def result = ''
        Browser.drive {
            result = page.rekommendationer.rehabYes.value();
        }
        result
    }

    String rehabNej() {
        def result = ''
        Browser.drive {
            result = page.rekommendationer.rehabNo.value();
        }
        result
    }

    boolean rehabNejVisas() {
        def result
        Browser.drive {
            result = page.rekommendationer.rehabNo.isDisplayed();
        }
        result
    }

    void klickaPaTillbakaKnappen() {
        Browser.drive {
            page.tillbaka()
        }
    }

    boolean textForIdInnehaller(String elementId, String expectedText) {
        def result;
        Browser.drive {
            def element = page.elementForId(elementId);
            result = containText(element, expectedText);
        }
        result
    }

    boolean containText(element, expectedText) {
        // println(element);
        if (element != null && element instanceof ArrayList) {
            element = element.get(0);
        }
        def text = '';
        if (element) {
            if (element.value()) {
                text = element.value();
            } else if (element.text()) {
                text = element.text();
            }
        }
        return text.contains(expectedText);
    }

    boolean textForClassInnehallar(String classId, String expectedText) {
        def result;
        Browser.drive {
            def element = page.elementForClass(classId);
            result = containText(element, expectedText);
        }
        result
    }

    /**
     *  Waits for the supplied element to both be displayed and contain a certain text.
     */
    def vantaPaAttElementMedIdInnehallerText(String elementId, String textToMatch) {
        Browser.drive {
            waitFor{
                $('#' + elementId, text: contains(textToMatch)).isDisplayed()
            }
        }
        true
    }

    /**
     *  Simple check to determine if a element with a known id is visible or not (without waiting).
     */
    boolean elementMedIdVisas(String elementId, boolean visas) {
        def result;
        Browser.drive {
            result = $('#' + elementId).isDisplayed()
        }
        result == visas
    }


    /**
     * Returns true if the specified text exists in any of the markup within the specified element id. E.g,
     * note that this looks at all HTML, not just text within the element.
     */
    boolean markupForIdInnehaller(String elementId, String text) {
        def result
        Browser.drive {
            def element = $('#' + elementId)
            result = element.contains(text)
        }
        result
    }

    void klickaPaSmittskyd(boolean val) {
        Browser.drive {
            page.setSmittskydd(val);
            waitFor {
                page.doneLoading()
            }
        }
    }

    boolean diagnosArSynligt() {
        def result
        Browser.drive {
            result = page.diagnos.isDisplayed()
        }
        result
    }

    /**
     * Clicks the anchor identified as having the supplied string as message key.
     *
     * @param messageKeyForLink
     * @return
     */
    void klickaPaFellank(String messageKeyForLink) {
        Browser.drive {
            def errorLink = $('a').find('span[key="' + messageKeyForLink + '"]');
            errorLink.click()
        }
    }

    /**
     * Checks the full markup of the <body> for the text string "[Missing " and returns
     * false if it is found.
     */
    boolean ingaOversattningsnycklarSaknas() {
        def result
        Browser.drive {
            result = $('body').text().contains("[Missing ")
        }
        !result
    }
}
