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

package se.inera.intyg.webcert.specifications.pages.fk7263

import se.inera.intyg.webcert.specifications.pages.AbstractViewCertPage

class VisaFk7263Page extends AbstractViewCertPage {

    static at = { doneLoading() && $("#viewCertAndQA").isDisplayed() }

    static content = {

        // messages
        intygFel { $("#cert-inline-error") }

        certificateIsSentToITMessage(required: false, wait: true) { $("#certificate-is-sent-to-it-message-text") }
        certificateIsSentToRecipientMessage(required: false, wait: true) { $("#certificate-is-sent-to-recipient-message-text") }
        certificateIsRevokedMessage(required: false, wait: true) { $("#certificate-is-revoked-message-text") }
        certificateIsOnQueueToITMessage(required: false) { $('#certificate-is-on-sendqueue-to-it-message-text') }

        visaVadSomSaknasLista(required: false) { $("#visa-vad-som-saknas-lista") }
        visaVadSomSaknasListaNoWait{$("#visa-vad-som-saknas-lista")}

        skickaDialogBody { $("span[key=\"fk7263.label.send.body\"]") }

        // kopiera dialog text webcert-1449
        copyDialog { $("#copy-dialog") }

        // Vidarebefordra utkast
        vidarebefordraEjHanterad(required: false) { $("#vidarebefordraEjHanterad") }

        // fraga svar
        nyFragaSvarKnapp { $("#askQuestionBtn") }
        nyFragaFrageText { $("#newQuestionText") }
        nyFragaFrageAmne { $("#new-question-topic") }
        nyFragaSkickaFragaKnapp { $("#sendQuestionBtn") }
        nyFragaSkickadTextruta { $("#question-is-sent-to-fk-message-text") }

        // INTYG
        nyttPersonnummer { $("#nyttPersonnummer") }
        namnOchPersonnummer { $("#patientNamnPersonnummer") }

        // smittskydd
        smittskydd { $("#smittskydd") }

        // diagnos
        diagnosKod { $("#diagnosKod") }
        diagnosBeskrivning { $("#diagnosBeskrivning") }
        diagnosKod2 { $("#diagnosKod2") }
        diagnosBeskrivning2 { $("#diagnosBeskrivning2") }
        diagnosKod3 { $("#diagnosKod3") }
        diagnosBeskrivning3 { $("#diagnosBeskrivning3") }
        samsjuklighet { $("#samsjuklighet") }

        // 3
        sjukdomsforlopp { $("#sjukdomsforlopp") }

        // 4a
        funktionsnedsattning { $("#funktionsnedsattning") }

        // 4b
        baseratPaList { $("#baseratPaList") }
        undersokningAvPatienten { $("#undersokningAvPatienten") }
        telefonkontaktMedPatienten { $("#telefonkontaktMedPatienten") }
        journaluppgifter { $("#journaluppgifter") }
        annanReferens { $("#annanReferens") }
        annanReferensBeskrivning { $("#annanReferensBeskrivning") }

        // 5
        aktivitetsbegransning { $("#aktivitetsbegransning") }

        // 6
        rekommendationKontaktArbetsformedlingen { $("#rekommendationKontaktArbetsformedlingen") }
        rekommendationKontaktForetagshalsovarden { $("#rekommendationKontaktForetagshalsovarden") }
        rekommendationOvrigt { $("#rekommendationOvrigt") }
        atgardInomSjukvarden { $("#atgardInomSjukvarden") }
        annanAtgard { $("#annanAtgard") }

        // 7
        rehabiliteringAktuell{$("#rehabiliteringAktuell") }
        rehabiliteringEjAktuell{$("#rehabiliteringEjAktuell") }
        rehabiliteringGarInteAttBedoma{$("#rehabiliteringGarInteAttBedoma") }

        // 8
        nuvarandeArbetsuppgifter { $("#nuvarandeArbetsuppgifter") }
        arbetsloshet { $("#arbetsloshet") }
        foraldrarledighet { $("#foraldrarledighet") }
        nedsattMed25from { $("#nedsattMed25from") }
        nedsattMed25tom { $("#nedsattMed25tom") }
        nedsattMed25Beskrivning { $("#nedsattMed25Beskrivning") }
        nedsattMed50from { $("#nedsattMed50from") }
        nedsattMed50tom { $("#nedsattMed50tom") }
        nedsattMed50Beskrivning { $("#nedsattMed50Beskrivning") }
        nedsattMed75from { $("#nedsattMed75from") }
        nedsattMed75tom { $("#nedsattMed75tom") }
        nedsattMed75Beskrivning { $("#nedsattMed75Beskrivning") }
        nedsattMed100from { $("#nedsattMed100from") }
        nedsattMed100tom { $("#nedsattMed100tom") }

        // 9
        arbetsformagaPrognos { $("#arbetsformagaPrognos") }

        // 10
        arbetsformagaPrognosJa{$("#arbetsformataPrognosJa") }
        arbetsformagaPrognosJaDelvis{$("#arbetsformataPrognosJaDelvis") }
        arbetsformagaPrognosNej{$("#arbetsformataPrognosNej") }
        arbetsformagaPrognosGarInteAttBedoma{$("#arbetsformataPrognosGarInteAttBedoma") }
        arbetsformagaPrognosGarInteAttBedomaBeskrivning{$("#arbetsformagaPrognosGarInteAttBedomaBeskrivning") }

        // 11
        ressattTillArbeteAktuellt{$("#ressattTillArbeteAktuellt")}
        ressattTillArbeteEjAktuellt{$("#ressattTillArbeteEjAktuellt")}

        // 12
        kontaktMedFk { $("#kontaktMedFk") }

        // 13
        kommentar { $("#kommentar") }

        // 17
        forskrivarkodOchArbetsplatskod { $("#forskrivarkodOchArbetsplatskod") }

        // 14
        signeringsdatum { $("#signeringsdatum") }

        // 15,16
        vardperson_namn { $("#vardperson_namn") }
        vardperson_enhetsnamn { $("#vardperson_enhetsnamn") }
        vardperson_postadress { $("#vardperson_postadress") }
        vardperson_postnummer { $("#vardperson_postnummer") }
        vardperson_postort { $("#vardperson_postort") }
        vardperson_telefonnummer { $("#vardperson_telefonnummer") }
    }

    def sendWithValidation() {
        skickaKnapp.click()
        waitFor {
            doneLoading()
            skickaDialogBody.text().contains("Försäkringskassan.")
            skickaDialogSkickaKnapp.isEnabled()
        }
        skickaDialogSkickaKnapp.click()
    }

    def stallNyFragaTillForsakringskassan() {
        nyFragaSvarKnapp.click()
        waitFor {
            doneLoading()
        }
    }

    boolean nyFragaTillForsakringskassanFormularVisas(boolean expected = true) {
        def result;
        waitFor {
            nyFragaFrageText.isDisplayed()
        }
        expected == nyFragaFrageText.isDisplayed()
    }

    def fillNyFragaFormular() {
        waitFor {
            nyFragaTillForsakringskassanFormularVisas(true)
        }
        nyFragaFrageText.value("Kan vi boka in ett möte med alla inblandade 15/5 15:00 på FK kontor?")
        nyFragaFrageAmne.value("2")
        nyFragaSkickaFragaKnapp.click()

        waitFor {
            nyFragaSkickadTextruta.isDisplayed()
        }
    }

    boolean nyFragaSkickadTextVisas(boolean expected = true) {
        waitFor {
            nyFragaSkickadTextruta.isDisplayed()
        }
        expected == nyFragaSkickadTextruta.isDisplayed()
    }
}
