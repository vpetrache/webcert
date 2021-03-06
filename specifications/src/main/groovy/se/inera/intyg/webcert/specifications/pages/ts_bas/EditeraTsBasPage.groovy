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

package se.inera.intyg.webcert.specifications.pages.ts_bas

import geb.Module
import se.inera.intyg.webcert.specifications.page.AbstractPage
import se.inera.intyg.webcert.specifications.pages.AbstractEditCertPage
import se.inera.intyg.webcert.specifications.pages.VardenhetModule

class EditeraTsBasPage extends AbstractEditCertPage {

    static at = { doneLoading() && $("#edit-ts-bas").isDisplayed() }

    static content = {

        // Formulärfält
        form { $("form") }
        patient { module PatientModule }
        intygetAvser { module IntygetAvserModule }
        identitet { module IdentitetModule }
        syn { module SynModule }
        horselBalans { module HorselBalansModule }
        funktionsnedsattning { module FunktionsnedsattningModule }
        hjartkarl { module HjartkartModule }
        diabetes { module DiabetesModule }
        neurologi { module NeurologiModule }
        medvetandestorning { module MedvetandestorningModule }
        njurar { module NjurarModule }
        kognitivt { module KognitivtModule }
        somnvakenhet { module SomnvakenhetModule }
        narkotikaLakemedel { module NarkotikaLakemedelModule }
        psykiskt { module PsykisktModule }
        utvecklingsstorning { module UtvecklingsstorningModule }
        sjukhusvard { module SjukhusvardModule }
        medicinering { module MedicineringModule }
        kommentar { $("#kommentar") }

        bedomning { name -> module BedomningModule, form: form }

        // Intygsvalidering
        valideringPatient(required: false){$("div[data-validation-section='patient']") }
        valideringIntygAvser(required: false){$("div[data-validation-section='intygavser']") }
        valideringIdentitet(required: false){$("div[data-validation-section='identitet']") }
        valideringSyn(required: false){$("div[data-validation-section='syn']") }
        valideringHorselBalans(required: false){$("div[data-validation-section='horselbalans']") }
        valideringFunktionsNedsattning(required: false){$("div[data-validation-section='funktionsnedsattning']") }
        valideringHjartkarl(required: false){$("div[data-validation-section='hjartkarl']") }
        valideringNeurologi(required: false){$("div[data-validation-section='neurologi']") }
        valideringMedvetandestorning(required: false){$("div[data-validation-section='medvetandestorning']") }
        valideringNjurar(required: false){$("div[data-validation-section='njurar']") }
        valideringKognitivt(required: false){$("div[data-validation-section='kognitivt']") }
        valideringSomnVakenhet(required: false){$("div[data-validation-section='somnvakenhet']") }
        valideringNarkotikaLakemedel(required: false){$("div[data-validation-section='narkotikalakemedel']") }
        valideringPsykiskt(required: false){$("div[data-validation-section='psykiskt']") }
        valideringUtvecklingsStorning(required: false){$("div[data-validation-section='utvecklingsstorning']") }
        valideringSjukhusVard(required: false){$("div[data-validation-section='sjukhusvard']") }
        valideringMedicinering(required: false){$("div[data-validation-section='medicinering']") }
        valideringBedomning(required: false){$("div[data-validation-section='bedomning']") }
        valideringVardEnhet(required: false){$("div[data-validation-section='vardenhet']") }
        valideringDiabetes(required: false){$("div[data-validation-section='diabetes']") }
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
        c1 { $("#typcheck0") }
        c1e { $("#typcheck1") }
        c { $("#typcheck2") }
        ce { $("#typcheck3") }
        d1 { $("#typcheck4") }
        d1e { $("#typcheck5") }
        d { $("#typcheck6") }
        de { $("#typcheck7") }
        taxi { $("#typcheck8") }
        annat { $("#typcheck9") }
    }

    def valjBehorigheter(String valdaBehorigheter) {
        if (valdaBehorigheter != null) {
            AbstractPage.scrollIntoView('intygetAvserForm');
            c1 = false
            c1e = false
            c = false
            ce = false
            d1 = false
            d1e = false
            d = false
            de = false
            taxi = false
            annat = false

            def behorigheter = valdaBehorigheter.split(",");

            AbstractEditCertPage.scrollIntoView("intygetAvserForm");

            if (behorigheter.contains("C1")) c1 = true
            if (behorigheter.contains("C1E")) c1e = true
            if (behorigheter.contains("C")) c = true
            if (behorigheter.contains("CE")) ce = true
            if (behorigheter.contains("D1")) d1 = true
            if (behorigheter.contains("D1E")) d1e = true
            if (behorigheter.contains("D")) d = true
            if (behorigheter.contains("DE")) de = true
            if (behorigheter.contains("Taxi")) taxi = true
            if (behorigheter.contains("Annat")) annat = true
        }
    }

    def hamtaBehorigheter() {
        def result = "";
        if (c1.value() == "on")    { if (result != "") { result += "," }; result += "C1" }
        if (c1e.value() == "on")   { if (result != "") { result += "," }; result += "C1E" }
        if (c.value() == "on")     { if (result != "") { result += "," }; result += "C" }
        if (ce.value() == "on")    { if (result != "") { result += "," }; result += "CE" }
        if (d1.value() == "on")    { if (result != "") { result += "," }; result += "D1" }
        if (d1e.value() == "on")   { if (result != "") { result += "," }; result += "D1E" }
        if (d.value() == "on")     { if (result != "") { result += "," }; result += "D" }
        if (de.value() == "on")    { if (result != "") { result += "," }; result += "DE" }
        if (taxi.value() == "on")  { if (result != "") { result += "," }; result += "Taxi" }
        if (annat.value() == "on") { if (result != "") { result += "," }; result += "Annat" }
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
            AbstractPage.scrollIntoView('identitetForm');
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

class SynModule extends Module {
    static base = { $("#synForm") }
    static content = {
        fragaA { $("input", name: "syna") }
        fragaB { $("input", name: "synb") }
        fragaC { $("input", name: "sync") }
        fragaD { $("input", name: "synd") }
        fragaE { $("input", name: "syne") }
        hogerOgaUtanKorrektion { $("#synHogerOgaUtanKorrektion") }
        hogerOgaMedKorrektion { $("#synHogerOgaMedKorrektion") }
        hogerOgaKontaktlins { $("#synHogerOgaKontaktlins") }
        vansterOgaUtanKorrektion { $("#synVansterOgaUtanKorrektion") }
        vansterOgaMedKorrektion { $("#synVansterOgaMedKorrektion") }
        vansterOgaKontaktlins { $("#synVasterOgaKontaktlins") }
        binokulartUtanKorrektion { $("#synBinokulartUtanKorrektion") }
        binokulartMedKorrektion { $("#synBinokulartMedKorrektion") }
        dioptrier { $("#dioptrier") }
    }
}

class HorselBalansModule extends Module {
    static base = { $("#horselBalansForm") }
    static content = {
        fragaA { $("input", name: "horselbalansa") }
        fragaB { $("input", name: "horselbalansb") }
    }
}

class FunktionsnedsattningModule extends Module {
    static base = { $("#funktionsnedsattningForm") }
    static content = {
        fragaA { $("input", name: "funktionsnedsattninga") }
        beskrivning { $("#funktionsnedsattning") }
        fragaB { $("input", name: "funktionsnedsattningb") }
    }
}

class HjartkartModule extends Module {
    static base = { $("#hjartkartForm") }
    static content = {
        fragaA { $("input", name: "hjartkarla") }
        fragaB { $("input", name: "hjartkarlb") }
        fragaC { $("input", name: "hjartkarlc") }
        beskrivning { $("#beskrivningRiskfaktorer") }
    }
}

class DiabetesModule extends Module {
    static base = { $("#diabetesForm") }
    static content = {
        fragaA { $("input", name: "diabetesa") }
        diabetestyp { $("input", name: "diabetestyp") }
        behandlingKost { $("#diabetestreat1") }
        behandlingTabletter { $("#diabetestreat2") }
        behandlingInsulin { $("#diabetestreat3") }
    }

    def valjTyp(String valdDiabetestyp) {
        if (valdDiabetestyp != null) {
            AbstractPage.scrollIntoView('diabetesForm');
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

class NeurologiModule extends Module {
    static base = { $("#neurologiForm") }
    static content = {
        fragaA { $("input", name: "neurologia") }
    }
}

class MedvetandestorningModule extends Module {
    static base = { $("#medvetandestorningForm") }
    static content = {
        fragaA { $("input", name: "medvetandestorninga") }
        beskrivning { $("#beskrivningMedvetandestorning") }
    }
}

class NjurarModule extends Module {
    static base = { $("#njurarForm") }
    static content = {
        fragaA { $("input", name: "njurara") }
    }
}

class KognitivtModule extends Module {
    static base = { $("#kognitivtForm") }
    static content = {
        fragaA { $("input", name: "kognitivta") }
    }
}

class SomnvakenhetModule extends Module {
    static base = { $("#somnvakenhetForm") }
    static content = {
        fragaA { $("input", name: "somnvakenheta") }
    }
}

class NarkotikaLakemedelModule extends Module {
    static base = { $("#narkotikaLakemedelForm") }
    static content = {
        fragaA { $("input", name: "narkotikalakemedela") }
        fragaB { $("input", name: "narkotikalakemedelb") }
        fragaB2 { $("input", name: "narkotikalakemedelb2") }
        fragaC { $("input", name: "narkotikalakemedelc") }
        beskrivning { $("#beskrivningNarkotikalakemedel") }
    }
}

class PsykisktModule extends Module {
    static base = { $("#psykisktForm") }
    static content = {
        fragaA { $("input", name: "psykiskta") }
    }
}

class UtvecklingsstorningModule extends Module {
    static base = { $("#utvecklingsstorningForm") }
    static content = {
        fragaA { $("input", name: "utvecklingsstorninga") }
        fragaB { $("input", name: "utvecklingsstorningb") }
    }
}

class SjukhusvardModule extends Module {
    static base = { $("#sjukhusvardForm") }
    static content = {
        fragaA { $("input", name: "sjukhusvarda") }
        tidpunkt { $("#tidpunkt") }
        vardinrattning { $("#vardinrattning") }
        anledning { $("#anledning") }
    }
}

class MedicineringModule extends Module {
    static base = { $("#medicineringForm") }
    static content = {
        fragaA { $("input", name: "medicineringa") }
        beskrivning { $("#beskrivningMedicinering") }
    }
}

class BedomningModule extends Module {
    def form
    static base = { $("#bedomningForm") }
    static content = {

        behorighet { $("input", name: "behorighet") }
        behorighetGroup { form.behorighet }
        behorighetBedomning { $("#behorighet_bedomning") }
        behorighetKanInteTaStallning { $("#behorighet_kanintetastallning") }
        c1 { $("#korkortstyp0") }
        c1e { $("#korkortstyp1") }
        c { $("#korkortstyp2") }
        ce { $("#korkortstyp3") }
        d1 { $("#korkortstyp4") }
        d1e { $("#korkortstyp5") }
        d { $("#korkortstyp6") }
        de { $("#korkortstyp7") }
        taxi { $("#korkortstyp8") }
        annat { $("#korkortstyp9") }
        specialist { $("#specialist") }
    }

    def valjBehorighet(Boolean value) {
        if (value != null) {
            AbstractPage.scrollIntoView("behorighet_bedomning");
            behorighet = value;
        }
    }

    def valjBehorigheter(String valdaBehorigheter) {
        if (valdaBehorigheter != null) {
            AbstractPage.scrollIntoView('bedomningForm');
            c1 = false
            c1e = false
            c = false
            ce = false
            d1 = false
            d1e = false
            d = false
            de = false
            taxi = false
            annat = false

            def behorigheter = valdaBehorigheter.split(",");

            if (behorigheter.contains("C1")) c1 = true
            if (behorigheter.contains("C1E")) c1e = true
            if (behorigheter.contains("C")) c = true
            if (behorigheter.contains("CE")) ce = true
            if (behorigheter.contains("D1")) d1 = true
            if (behorigheter.contains("D1E")) d1e = true
            if (behorigheter.contains("D")) d = true
            if (behorigheter.contains("DE")) de = true
            if (behorigheter.contains("Taxi")) taxi = true
            if (behorigheter.contains("Annat")) annat = true
        }
    }
}
