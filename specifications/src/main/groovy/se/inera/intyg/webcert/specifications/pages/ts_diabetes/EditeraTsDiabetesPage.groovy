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

package se.inera.intyg.webcert.specifications.pages.ts_diabetes

import geb.Module
import se.inera.intyg.webcert.specifications.page.AbstractPage
import se.inera.intyg.webcert.specifications.pages.AbstractEditCertPage
import se.inera.intyg.webcert.specifications.pages.VardenhetModule

class EditeraTsDiabetesPage extends AbstractEditCertPage {

    static at = { doneLoading() && $("#edit-ts-diabetes").isDisplayed() }

    static content = {

        // Formulärfält
        form { $("form") }
        patient { module PatientModule }
        intygetAvser { module IntygetAvserModule }
        identitet { module IdentitetModule }
        allmant { module AllmantModule }
        hypoglykemier { module HypoglykemierModule }
        syn { module SynModule }
        bedomning { name -> module BedomningModule, form: form }
        kommentar { $("#kommentar") }
        specialist { $("#specialist") }

        // Intygsvalidering
        valideringPatient(required: false){$("div[data-validation-section='patient']") }
        valideringIntygAvser(required: false){$("div[data-validation-section='intygavser']") }
        valideringIdentitet(required: false){$("div[data-validation-section='identitet']") }
        valideringDiabetes(required: false){$("div[data-validation-section='diabetes']") }
        valideringHypoglykemier(required: false){$("div[data-validation-section='hypoglykemier']") }
        valideringSyn(required: false){$("div[data-validation-section='syn']") }
        valideringBedomning(required: false){$("div[data-validation-section='bedomning']") }
        valideringVardEnhet(required: false){$("div[data-validation-section='vardenhet']") }
    }
}

class PatientModule extends Module {
    static base = { $("#patientForm") }
    static content = {
        postadress { $("#patientPostadress") }
        postnummer { $("#patientPostnummer") }
        postort { $("#patientPostort") }
    }
}

class IntygetAvserModule extends Module {
    static base = { $("#intygetAvserForm") }
    static content = {
        am { $("#typcheck0") }
        a1 { $("#typcheck1") }
        a2 { $("#typcheck2") }
        a { $("#typcheck3") }
        b { $("#typcheck4") }
        be { $("#typcheck5") }
        traktor { $("#typcheck6") }
        c1 { $("#typcheck7") }
        c1e { $("#typcheck8") }
        c { $("#typcheck9") }
        ce { $("#typcheck10") }
        d1 { $("#typcheck11") }
        d1e { $("#typcheck12") }
        d { $("#typcheck13") }
        de { $("#typcheck14") }
        taxi { $("#typcheck15") }
    }

    def valjBehorigheter(String valdaBehorigheter) {
        if (valdaBehorigheter != null) {

            // scroll into view
            AbstractPage.scrollIntoView("typcheck0");

            am = false
            a1 = false
            a2 = false
            a = false
            b = false
            be = false
            traktor = false
            c1 = false
            c1e = false
            c = false
            ce = false
            d1 = false
            d1e = false
            d = false
            de = false
            taxi = false

            def behorigheter = valdaBehorigheter.split(",");

            if (behorigheter.contains("AM")) am = true
            if (behorigheter.contains("A1")) a1 = true
            if (behorigheter.contains("A2")) a2 = true
            if (behorigheter.contains("A")) a = true
            if (behorigheter.contains("B")) b = true
            if (behorigheter.contains("BE")) be = true
            if (behorigheter.contains("Traktor")) traktor = true
            if (behorigheter.contains("C1")) c1 = true
            if (behorigheter.contains("C1E")) c1e = true
            if (behorigheter.contains("C")) c = true
            if (behorigheter.contains("CE")) ce = true
            if (behorigheter.contains("D1")) d1 = true
            if (behorigheter.contains("D1E")) d1e = true
            if (behorigheter.contains("D")) d = true
            if (behorigheter.contains("DE")) de = true
            if (behorigheter.contains("Taxi")) taxi = true
        }
    }

    def hamtaBehorigheter() {
        def result = "";
        if (am.value() == "on")      { if (result != "") { result += "," }; result += "AM" }
        if (a1.value() == "on")      { if (result != "") { result += "," }; result += "A1" }
        if (a2.value() == "on")      { if (result != "") { result += "," }; result += "A2" }
        if (a.value() == "on")       { if (result != "") { result += "," }; result += "A" }
        if (b.value() == "on")       { if (result != "") { result += "," }; result += "B" }
        if (be.value() == "on")      { if (result != "") { result += "," }; result += "BE" }
        if (traktor.value() == "on") { if (result != "") { result += "," }; result += "Traktor" }
        if (c1.value() == "on")      { if (result != "") { result += "," }; result += "C1" }
        if (c1e.value() == "on")     { if (result != "") { result += "," }; result += "C1E" }
        if (c.value() == "on")       { if (result != "") { result += "," }; result += "C" }
        if (ce.value() == "on")      { if (result != "") { result += "," }; result += "CE" }
        if (d1.value() == "on")      { if (result != "") { result += "," }; result += "D1" }
        if (d1e.value() == "on")     { if (result != "") { result += "," }; result += "D1E" }
        if (d.value() == "on")       { if (result != "") { result += "," }; result += "D" }
        if (de.value() == "on")      { if (result != "") { result += "," }; result += "DE" }
        if (taxi.value() == "on")    { if (result != "") { result += "," }; result += "Taxi" }
        result
    }
}

class IdentitetModule extends Module {
    static base = { $("#identitetForm") }
    static content = {
        idkort { $("#identity0") }
        foretagskortTjansterkort { $("#identity1") }
        korkort { $("#identity2") }
        personligKannedom { $("#identity3") }
        forsakran { $("#identity4") }
        pass { $("#identity5") }
    }

    def valjTyp(String identifieringstyp) {
        if (identifieringstyp != null) {

            AbstractPage.scrollIntoView('identity0');
            def validTypes = ["idkort", "foretagskort", "korkort", "kannedom", "forsakran", "pass"]
            assert validTypes.contains(identifieringstyp),
                    "Fältet 'identifieringstyp' kan endast innehålla något av följande värden: ${validTypes}"

            if ("idkort" == identifieringstyp) {
                idkort.click()
            } else if ("foretagskort" == identifieringstyp) {
                foretagskortTjansterkort.click()
            } else if ("korkort" == identifieringstyp) {
                korkort.click()
            } else if ("kannedom" == identifieringstyp) {
                personligKannedom.click()
            } else if ("forsakran" == identifieringstyp) {
                forsakran.click()
            } else if ("pass" == identifieringstyp) {
                pass.click()
            }
        }
    }
}

class AllmantModule extends Module {
    static base = { $("#allmantForm") }
    static content = {
        ar { $("#diabetesyear") }
        diabetestyp { $("input", name: "diabetestyp") }
        behandlingKost { $("#diabetestreat1") }
        behandlingTabletter { $("#diabetestreat2") }
        behandlingInsulin { $("#diabetestreat3") }
        behandlingInsulinPeriod { $("#insulinBehandlingsperiod") }
        behandlingAnnan { $("#annanBehandlingBeskrivning") }
    }

    def valjTyp(String valdDiabetestyp) {
        if (valdDiabetestyp != null) {
            AbstractPage.scrollIntoView('allmantForm');
            def validTypes = ["typ1", "typ2"]
            assert validTypes.contains(valdDiabetestyp),
                    "Fältet 'diabetestyp' kan endast innehålla något av följande värden: ${validTypes}"

            if ("typ1" == valdDiabetestyp) {
                diabetestyp = "DIABETES_TYP_1"
            } else if ("typ2" == valdDiabetestyp) {
                diabetestyp = "DIABETES_TYP_2"
            }
        }
    }
}

class HypoglykemierModule extends Module {
    static base = { $("#hypoglykemierForm") }
    static content = {
        fragaA { $("input", name: "hypoa") }
        fragaB { $("input", name: "hypob") }
        fragaC { $("input", name: "hypoc") }
        fragaD { $("input", name: "hypod") }
        allvarligForekomstEpisoder { $("#allvarligForekomstBeskrivning") }
        fragaE { $("input", name: "hypoe") }
        allvarligForekomstTrafikEpisoder { $("#allvarligForekomstTrafikBeskrivning") }
        fragaF { $("input", name: "hypof") }
        fragaG { $("input", name: "hypog") }
        allvarligForekomstVakenTid { $("#allvarligForekomstVakenTidObservationstid") }
        allvarligForekomstVakenTidObservationstid_toggle { $("#allvarligForekomstVakenTidObservationstid-toggle") }
    }
}

class SynModule extends Module {
    static base = { $("#synForm") }
    static content = {
        fragaA { $("input", name: "syna") }
        fragaB { $("input", name: "synb") }
        hogerOgaUtanKorrektion { $("#synHogerOgaUtanKorrektion") }
        hogerOgaMedKorrektion { $("#synHogerOgaMedKorrektion") }
        vansterOgaUtanKorrektion { $("#synVansterOgaUtanKorrektion") }
        vansterOgaMedKorrektion { $("#synVansterOgaMedKorrektion") }
        binokulartUtanKorrektion { $("#synBinokulartUtanKorrektion") }
        binokulartMedKorrektion { $("#synBinokulartMedKorrektion") }
        fragaD { $("input", name: "synd") }
    }

    def valjFragaD(Boolean value) {
        if (value != null) {
            AbstractPage.scrollIntoView("synBinokulartMedKorrektion");
            fragaD = value;
        }
    }
}

class BedomningModule extends Module {
    def form
    static base = { $("#bedomningForm") }
    static content = {

        behorighet { $("input", name: "behorighet") }
        behorighetBedomning { $("#behorighet_bedomning") }
        behorighetKanInteTaStallning { $("#behorighet_kanintetastallning") }

        behorighetGroup { form.behorighet }

        am { $("#korkortstyp0") }
        a1 { $("#korkortstyp1") }
        a2 { $("#korkortstyp2") }
        a { $("#korkortstyp3") }
        b { $("#korkortstyp4") }
        be { $("#korkortstyp5") }
        traktor { $("#korkortstyp6") }
        c1 { $("#korkortstyp7") }
        c1e { $("#korkortstyp8") }
        c { $("#korkortstyp9") }
        ce { $("#korkortstyp10") }
        d1 { $("#korkortstyp11") }
        d1e { $("#korkortstyp12") }
        d { $("#korkortstyp13") }
        de { $("#korkortstyp14") }
        taxi { $("#korkortstyp15") }
        bedomning { $("input", name:  "bedomning") }
    }

    def valjBehorighet(Boolean value) {
        if (value != null) {
            AbstractPage.scrollIntoView("behorighet_bedomning");
            behorighet = value;
        }
    }

    def valjBehorigheter(String valdaBehorigheter) {
        if (valdaBehorigheter != null) {
            AbstractPage.scrollIntoView('korkortstyp0');
            am = false
            a1 = false
            a2 = false
            a = false
            b = false
            be = false
            traktor = false
            c1 = false
            c1e = false
            c = false
            ce = false
            d1 = false
            d1e = false
            d = false
            de = false
            taxi = false

            def behorigheter = valdaBehorigheter.split(",");

            if (behorigheter.contains("AM")) am = true
            if (behorigheter.contains("A1")) a1 = true
            if (behorigheter.contains("A2")) a2 = true
            if (behorigheter.contains("A")) a = true
            if (behorigheter.contains("B")) b = true
            if (behorigheter.contains("BE")) be = true
            if (behorigheter.contains("Traktor")) traktor = true
            if (behorigheter.contains("C1")) c1 = true
            if (behorigheter.contains("C1E")) c1e = true
            if (behorigheter.contains("C")) c = true
            if (behorigheter.contains("CE")) ce = true
            if (behorigheter.contains("D1")) d1 = true
            if (behorigheter.contains("D1E")) d1e = true
            if (behorigheter.contains("D")) d = true
            if (behorigheter.contains("DE")) de = true
            if (behorigheter.contains("Taxi")) taxi = true
        }
    }
}
