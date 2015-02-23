package se.inera.webcert.spec

import se.inera.webcert.spec.util.WebcertRestUtils;
import se.inera.webcert.spec.util.RestClientFixture
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC
import static se.inera.webcert.spec.util.WebcertRestUtils.*

import org.apache.commons.io.IOUtils

class SparaUtkast extends RestClientFixture {

    String intygId
    String intygTyp

    String patientPersonnummer
    String patientFornamn = "Test"
    String patientEfternamn = "Testsson"

    // hosperson
    String hsaId
    String namn

    // Enhet
    String enhetId = "SE4815162344-1A02"
    String enhetnamn = "WebCert-Integration Enhet 1"
    String telefonnummer = "123456789"
    String postadress = "Storgatan 12"
    String postnummer = "12345"
    String postort = "Ankeborg"
    String arbetsplatskod = "arbetsplatskod"
    String epost = "enhet1@webcert.se.invalid"

    // vardgivare
    String vardgivarId = "SE4815162344-1A01"
    String vardgivarnamn = "WebCert-Integration Vårdgivare 1"

    // Diagnos
    String diagnosKod
    String diagnosBeskrivning1
    
    String nedsattMed100 = null

    boolean komplett
    String kodverk = "ICD_10_SE"

    public setIntygId (String value) {
        intygId = value
    }
    public setIntygTyp(String value) {
        intygTyp = value;
    }
    public setPatientPersonnummer(String value) {
        patientPersonnummer = value;
    }
    public setPatientFornamn(String value) {
        patientFornamn = value;
    }
    public setPatientEfternamn(String value) {
        patientEfternamn = value;
    }
    public setHhsaId(String value) {
        hsaId = value;
    }
    public setNamn(String value) {
        namn = value;
    }
    public setEnhetId(String value) {
        enhetId = value;
    }
    public setKomplett(boolean value ) {
        komplett = value
    }
    def response
    def json

    public void execute() {
        WebcertRestUtils.login("user1")
        if (komplett) {
            makeComplete()
        } else {
            makeIncomplete()
        }
        response = WebcertRestUtils.saveUtkast(intygTyp, intygId, json)
    }

    public boolean utkastSparat() {
        response.success
    }

    // Fill utkast and set it up as 'complete'
    def makeComplete() {
        if ("fk7263" == intygTyp) {
            String diagnos = """"diagnosKodsystem1":"ICD_10_SE","diagnosKod":"$diagnosKod","diagnosBeskrivning1" : "$diagnosBeskrivning1", "diagnosKodsystem2":"ICD_10_SE","diagnosKod2" : "Z233",  "diagnosBeskrivning2" : "Vaccination avseende pest", "diagnosKodsystem3":"ICD_10_SE","diagnosKod3" : "Z321", "diagnosBeskrivning3" : "Graviditet bekräftad"""
            if (kodverk == "KSH_97_P") {
                diagnos = """"diagnosKodsystem1":"$kodverk","diagnosKod":"S50","diagnosBeskrivning1" : "Kontusion på armbåge", "diagnosKodsystem2":"$kodverk", "diagnosKod2" : "Z233",  "diagnosBeskrivning2" : "Vaccination avseende pest", "diagnosKodsystem3":"$kodverk", "diagnosKod3" : "Z321", "diagnosBeskrivning3" : "Graviditet bekräftad"""
            }
            if (nedsattMed100 != null) {
                json = """{"id":"$intygId","grundData":{"skapadAv":{"personId":"$hsaId","forskrivarKod":"1234567890","fullstandigtNamn":"$namn","vardenhet":{"enhetsid":"$enhetId","enhetsnamn":"$enhetnamn","arbetsplatsKod":"$arbetsplatskod","postadress":"$postadress","postnummer":"$postnummer","postort":"$postort","telefonnummer":"$telefonnummer","epost":"$epost","vardgivare":{"vardgivarid":"$vardgivarId","vardgivarnamn":"$vardgivarnamn"}}},"patient":{"personId":"$patientPersonnummer","fullstandigtNamn":"$patientFornamn $patientEfternamn","fornamn":"$patientFornamn","efternamn":"$patientEfternamn","postadress":"Adressen","postnummer":"22222","postort":"Hemma"}},"avstangningSmittskydd":false,"rekommendationKontaktArbetsformedlingen":false,"rekommendationKontaktForetagshalsovarden":false,"arbetsloshet":false,"foraldrarledighet":false,"ressattTillArbeteAktuellt":false,"ressattTillArbeteEjAktuellt":false,"kontaktMedFk":false,"namnfortydligandeOchAdress":"$patientFornamn $patientEfternamn\\n$enhetnamn","undersokningAvPatienten":"2014-05-06", "annanReferens": "2014-05-07", "annanReferensBeskrivning" : "Mailkontakt med patienten", "diagnosBeskrivning":"Skada underarm", $diagnos", "sjukdomsforlopp":"Trillade och skrapade armen mot trottoaren","funktionsnedsattning":"Kan inte lyfta armen","nuvarandeArbete":true,"nuvarandeArbetsuppgifter":"Armlyftare",$nedsattMed100,"prognosBedomning" : "arbetsformagaPrognosGarInteAttBedoma", "arbetsformagaPrognosGarInteAttBedomaBeskrivning" : "Oklart varför", "arbetsformagaPrognos" : "Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.", "aktivitetsbegransning" : "aktivitetsbegransning"}"""
            } else { 
                json = """{"id":"$intygId","grundData":{"skapadAv":{"personId":"$hsaId","forskrivarKod":"1234567890","fullstandigtNamn":"$namn","vardenhet":{"enhetsid":"$enhetId","enhetsnamn":"$enhetnamn","arbetsplatsKod":"$arbetsplatskod","postadress":"$postadress","postnummer":"$postnummer","postort":"$postort","telefonnummer":"$telefonnummer","epost":"$epost","vardgivare":{"vardgivarid":"$vardgivarId","vardgivarnamn":"$vardgivarnamn"}}},"patient":{"personId":"$patientPersonnummer","fullstandigtNamn":"$patientFornamn $patientEfternamn","fornamn":"$patientFornamn","efternamn":"$patientEfternamn","postadress":"Adressen","postnummer":"22222","postort":"Hemma"}},"avstangningSmittskydd":false,"rekommendationKontaktArbetsformedlingen":false,"rekommendationKontaktForetagshalsovarden":false,"arbetsloshet":false,"foraldrarledighet":false,"ressattTillArbeteAktuellt":false,"ressattTillArbeteEjAktuellt":false,"kontaktMedFk":false,"namnfortydligandeOchAdress":"$patientFornamn $patientEfternamn\\n$enhetnamn","undersokningAvPatienten":"2014-05-06", "annanReferens": "2014-05-07", "annanReferensBeskrivning" : "Mailkontakt med patienten", "diagnosBeskrivning":"Skada underarm", $diagnos", "sjukdomsforlopp":"Trillade och skrapade armen mot trottoaren","funktionsnedsattning":"Kan inte lyfta armen","nuvarandeArbete":true,"nuvarandeArbetsuppgifter":"Armlyftare","prognosBedomning" : "arbetsformagaPrognosGarInteAttBedoma", "arbetsformagaPrognosGarInteAttBedomaBeskrivning" : "Oklart varför", "arbetsformagaPrognos" : "Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.", "aktivitetsbegransning" : "aktivitetsbegransning"}"""
            }
        }
    }

    def makeIncomplete() {
        if ("fk7263" == intygTyp) {
            json = """{"id":"$intygId","grundData":{"skapadAv":{"personId":"$hsaId","forskrivarKod":"1234567890","fullstandigtNamn":"$namn","vardenhet":{"enhetsid":"$enhetId","enhetsnamn":"$enhetnamn","arbetsplatsKod":"$arbetsplatskod","postadress":"$postadress","postnummer":"$postnummer","postort":"$postort","telefonnummer":"$telefonnummer","epost":"$epost","vardgivare":{"vardgivarid":"$vardgivarId","vardgivarnamn":"$vardgivarnamn"}}},"patient":{"personId":"$patientPersonnummer","fullstandigtNamn":"$patientFornamn $patientEfternamn","fornamn":"$patientFornamn","efternamn":"$patientEfternamn","postadress":"Adressen","postnummer":"22222","postort":"Hemma"}},"avstangningSmittskydd":false,"rekommendationKontaktArbetsformedlingen":false,"rekommendationKontaktForetagshalsovarden":false,"arbetsloshet":false,"foraldrarledighet":false,"ressattTillArbeteAktuellt":false,"ressattTillArbeteEjAktuellt":false,"kontaktMedFk":false,"namnfortydligandeOchAdress":"$patientFornamn $patientEfternamn\\n$enhetnamn","undersokningAvPatienten":"2014-05-06", "annanReferens": "2014-05-07", "annanReferensBeskrivning" : "Mailkontakt med patienten", "sjukdomsforlopp":"Trillade och skrapade armen mot trottoaren","funktionsnedsattning":"Kan inte lyfta armen","nuvarandeArbete":true,"nuvarandeArbetsuppgifter":"Armlyftare","prognosBedomning" : "arbetsformagaPrognosGarInteAttBedoma", "arbetsformagaPrognosGarInteAttBedomaBeskrivning" : "Oklart varför", "arbetsformagaPrognos" : "Skadan har förvärrats vid varje tillfälle patienten använt armen. Måste hållas i total stillhet tills läkningsprocessen kommit en bit på väg. Eventuellt kan utredning visa att operation är nödvändig för att läka skadan.", "aktivitetsbegransning" : "aktivitetsbegransning"}"""
        }
    }
}
