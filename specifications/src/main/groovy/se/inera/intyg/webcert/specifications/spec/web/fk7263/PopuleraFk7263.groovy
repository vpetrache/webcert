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

package se.inera.intyg.webcert.specifications.spec.web.fk7263

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.openqa.selenium.Keys
import se.inera.intyg.webcert.specifications.page.AbstractPage
import se.inera.intyg.webcert.specifications.pages.fk7263.EditeraFk7263Page
import se.inera.intyg.webcert.specifications.spec.Browser
import se.inera.intyg.webcert.specifications.spec.util.screenshot.ExceptionHandlingFixture

class PopuleraFk7263 extends ExceptionHandlingFixture {

    public static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Boolean smittskydd
    Boolean undersokning
    String undersokningDatum
    Boolean telefonkontakt
    String telefonkontaktDatum
    Boolean journal
    String journalDatum
    Boolean other
    String otherDatum
    String otherText
    String diagnosKodverk
    String diagnos1
    String diagnos1Text
    String diagnos2
    String diagnos2Text
    String diagnos3
    String diagnos3Text
    String diagnosFortydligande
    Boolean diagnosSamsjuklighet
    String sjukdomsforlopp
    String funktionsnedsattning
    String aktivitetsbegransning
    Boolean nuvarandearbete
    String arbetsuppgifter
    Boolean arbetslos
    Boolean foraldraledig
    String tjanstgoringstid
    Boolean nedsattMed25
    Integer nedsattMed25start
    Integer nedsattMed25slut
    String nedsattMed25beskrivning
    Boolean nedsattMed50
    Integer nedsattMed50start
    Integer nedsattMed50slut
    String nedsattMed50beskrivning
    Boolean nedsattMed75
    Integer nedsattMed75start
    Integer nedsattMed75slut
    String nedsattMed75beskrivning
    Boolean nedsattMed100
    Integer nedsattMed100start
    Integer nedsattMed100slut
    String nedsattBeskrivning
    String prognos
    String prognosBeskrivning
    String atgardSjukvard
    String atgardAnnan
    Boolean rekommendationRessatt
    Boolean rekommendationKontaktAf
    Boolean rekommendationKontaktForetagshalsovard
    Boolean rekommendationOvrigt
    String rekommendationOvrigtBeskrivning
    String arbetslivsinriktadRehabilitering
    Boolean kontaktFk
    String ovrigt
    String vardenhetPostadress
    String vardenhetPostnummer
    String vardenhetPostort
    String vardenhetTelefonnummer
    String vardenhetEpost
    String recommendationsToFkTravel

    def execute() {
        Browser.drive {

            waitFor {
                at EditeraFk7263Page
            }

            page.setAutoSave(false);
            page.setSaving(true);


            if (smittskydd != null) page.setSmittskydd(smittskydd)

            if (undersokning != null){
                page.baserasPa.setUndersokningCheckBox(undersokning)
            }
            if (undersokningDatum != null) {
                page.baserasPa.undersokningDatum = undersokningDatum
                page.baserasPa.undersokningDatum << Keys.TAB
            }
            if (telefonkontakt != null){
                page.baserasPa.setTelefonkontaktCheckBox(telefonkontakt)
            }
            if (telefonkontaktDatum != null) {
                page.baserasPa.telefonkontaktDatum = telefonkontaktDatum
            }
            if (journal != null){
                page.baserasPa.setJournalCheckBox(journal)
            }
            if (journalDatum != null) {
                page.baserasPa.journalDatum = journalDatum
            }
            if (other != null) {
                page.baserasPa.setOtherCheckBox(other);
            }
            if (otherDatum != null) {
                page.baserasPa.otherDatum = otherDatum
            }
            if (otherText != null) {
                page.baserasPa.otherText = otherText
            }

            if (diagnosKodverk == 'ICD_10_SE') {
                page.diagnos.diagnoseKodverk_ICD_10_SE = true
            }
            else if (diagnosKodverk == 'KSH_97_P') {
                page.diagnos.diagnoseKodverk_KSH_97_P = true
            }
            if (diagnos1 != null) {
                waitFor{
                    page.diagnos.diagnos1.isDisplayed()
                }

                AbstractPage.scrollIntoView("diagnoseCode");
                page.diagnos.diagnos1 = diagnos1

                waitFor {
                    page.$('#diagnoseCode + UL').isDisplayed()
                }
                // diagnos1Text kommer sättas med enter i detta fält
                page.diagnos.diagnos1 << Keys.ENTER
            }
            if (diagnos1Text != null) {
                page.diagnos.diagnos1Text = diagnos1Text
                waitFor {
                    page.$('#diagnoseDescription + UL').isDisplayed()
                }
                page.diagnos.diagnos1Text << Keys.ENTER
            }
            if (diagnos2 != null) {
                page.diagnos.diagnos2 = diagnos2
                waitFor {
                    page.$('#diagnoseCodeOpt1 + UL').isDisplayed()
                }
                page.diagnos.diagnos2 << Keys.ENTER
            }
            if (diagnos2Text != null) {
                page.diagnos.diagnos2Text = diagnos2Text
                waitFor {
                    page.$('#diagnoseDescriptionOpt1 + UL').isDisplayed()
                }
                page.diagnos.diagnos2Text << Keys.ENTER
            }
            if (diagnos3 != null) {
                page.diagnos.diagnos3 = diagnos3
                waitFor {
                    page.$('#diagnoseCodeOpt2 + UL').isDisplayed()
                }
                page.diagnos.diagnos3 << Keys.ENTER
            }
            if (diagnos3Text != null) {
                page.diagnos.diagnos3Text = diagnos3Text
                waitFor {
                    page.$('#diagnoseDescriptionOpt2 + UL').isDisplayed()
                }
                page.diagnos.diagnos3Text << Keys.ENTER
            }
            if (diagnosFortydligande != null) page.diagnos.fortydligande = diagnosFortydligande

            if (diagnosSamsjuklighet != null) page.diagnos.samsjuklighet = diagnosSamsjuklighet

            if (sjukdomsforlopp != null) page.sjukdomsforlopp = sjukdomsforlopp

            if (funktionsnedsattning != null) {
                page.funktionsnedsattning = funktionsnedsattning
                page.funktionsnedsattning << Keys.TAB
            }

            if (aktivitetsbegransning != null){
                page.aktivitetsbegransning = aktivitetsbegransning
            }

            if (nuvarandearbete != null){
                page.arbete.setNuvarandeCheckBox(nuvarandearbete);
            }
            if (arbetsuppgifter != null) page.arbete.arbetsuppgifter = arbetsuppgifter
            if (arbetslos != null) page.arbete.arbetslos = arbetslos
            if (foraldraledig != null) page.arbete.foraldraledig = foraldraledig

            if (tjanstgoringstid != null) page.arbetsformaga.tjanstgoringstid = tjanstgoringstid
            if(nedsattMed25 != null){
                AbstractPage.scrollIntoView("nedsattMed25");
            }
            if (nedsattMed25 != null) page.arbetsformaga.nedsattMed25 = nedsattMed25
            if (nedsattMed25start != null) page.arbetsformaga.nedsattMed25start = toDate(nedsattMed25start)
            if (nedsattMed25slut != null) page.arbetsformaga.nedsattMed25slut = toDate(nedsattMed25slut)
            if (nedsattMed25beskrivning != null) page.arbetsformaga.nedsattMed25beskrivning = nedsattMed25beskrivning
            if(nedsattMed50 != null){
                AbstractPage.scrollIntoView("nedsattMed50");
            }
            if (nedsattMed50 != null) page.arbetsformaga.nedsattMed50 = nedsattMed50
            if (nedsattMed50start != null) page.arbetsformaga.nedsattMed50start = toDate(nedsattMed50start)
            if (nedsattMed50slut != null) page.arbetsformaga.nedsattMed50slut = toDate(nedsattMed50slut)
            if (nedsattMed50beskrivning != null) page.arbetsformaga.nedsattMed50beskrivning = nedsattMed50beskrivning
            if(nedsattMed75 != null){
                AbstractPage.scrollIntoView("nedsattMed75");
            }
            if (nedsattMed75 != null) page.arbetsformaga.nedsattMed75 = nedsattMed75
            if (nedsattMed75start != null) page.arbetsformaga.nedsattMed75start = toDate(nedsattMed75start)
            if (nedsattMed75slut != null) page.arbetsformaga.nedsattMed75slut = toDate(nedsattMed75slut)
            if (nedsattMed75beskrivning != null) page.arbetsformaga.nedsattMed75beskrivning = nedsattMed75beskrivning
            if(nedsattMed100 != null){
                AbstractPage.scrollIntoView("nedsattMed100");
                waitFor{
                    page.arbetsformaga.nedsattMed100.isDisplayed()
                }
            }
            if (nedsattMed100 != null) page.arbetsformaga.nedsattMed100 = nedsattMed100
            if (nedsattMed100start != null) page.arbetsformaga.nedsattMed100start = toDate(nedsattMed100start)
            if (nedsattMed100slut != null) page.arbetsformaga.nedsattMed100slut = toDate(nedsattMed100slut)

            if (nedsattBeskrivning != null) page.arbetsformagaBeskrivning = nedsattBeskrivning

            page.prognos.valjPrognos(prognos)
            if (prognosBeskrivning != null) page.prognos.beskrivning = prognosBeskrivning

            if (atgardSjukvard != null) page.atgardSjukvard = atgardSjukvard
            if (atgardAnnan != null) page.atgardAnnan = atgardAnnan

            if (rekommendationRessatt != null) {
                if (rekommendationRessatt) {
                    page.rekommendationer.ressattJa = true
                    page.rekommendationer.ressattNej = false
                } else {
                    page.rekommendationer.ressattJa = false
                    page.rekommendationer.ressattNej = true
                }
            }

            if (rekommendationKontaktAf != null || rekommendationKontaktForetagshalsovard != null || rekommendationOvrigt != null) {
                AbstractPage.scrollIntoView(page.rekommendationer.kontaktAf.attr("id"));
            }
            if (rekommendationKontaktAf != null) page.rekommendationer.kontaktAf = rekommendationKontaktAf
            if (rekommendationKontaktForetagshalsovard != null) page.rekommendationer.kontaktForetagshalsovard = rekommendationKontaktForetagshalsovard
            if (rekommendationOvrigt != null) page.rekommendationer.ovrigt = rekommendationOvrigt
            if (rekommendationOvrigtBeskrivning != null) page.rekommendationer.ovrigtBeskrivning = rekommendationOvrigtBeskrivning
            page.rekommendationer.valjArbetslivsinriktadRehabilitering(arbetslivsinriktadRehabilitering)
            page.rekommendationer.valjRecommendationsToFkTravel(recommendationsToFkTravel)


            if (kontaktFk != null) page.kontaktFk = kontaktFk
            if (ovrigt != null) page.ovrigt = ovrigt
            if (vardenhetPostadress != null) page.vardenhet.postadress = vardenhetPostadress
            if (vardenhetPostnummer != null) page.vardenhet.postnummer = vardenhetPostnummer
            if (vardenhetPostort != null) page.vardenhet.postort = vardenhetPostort
            if (vardenhetTelefonnummer != null) page.vardenhet.telefonnummer = vardenhetTelefonnummer
            if (vardenhetEpost != null) page.vardenhet.epost = vardenhetEpost

            page.setAutoSave(true);
            // after any updates we need to wait for saving before we can start checking the state of the page in a fixture
            page.doneSaving();
        }
    }

    String toDate(int relativeFromToday) {
        LocalDate.now().plusDays(relativeFromToday).format(fmt)
    }
}
