package se.inera.intyg.webcert.specifications.pages

class SokSkrivValjIntygTypPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#valj-intyg-typ").isDisplayed() }

    static content = {
        patientNamn { $("#patientNamn") }
        intygtypFortsattKnapp { $("#intygTypeFortsatt") }
        intygTyp { $("#intygType") }
        intygLista { $("#intygLista") }
        kopieraKnapp { intygId -> $("#copyBtn-${intygId}") }
        kopieraDialogVisaInteIgen { $("#dontShowAgain") }
        kopieraDialogKopieraKnapp { $("#button1copy-dialog") }
        fortsattKnapp { $("#intygTypeFortsatt") }
        felmeddelandeRuta { $("#current-list-noResults-error")}
        sekretessmarkering { $("#sekretessmarkering") }
    }

    def copy(String intygId) {
        kopieraKnapp(intygId).click()
        waitFor {
            doneLoading()
        }
        kopieraDialogKopieraKnapp.click()
        waitFor {
            doneLoading()
        }
    }

    void show(String intygId) {
        $("#showBtn-${intygId}").click()
        waitFor {
            doneLoading()
        }
    }

    def valjIntygstypFk7263() {
        intygTyp.value("string:fk7263")
    }
    def valjIntygstypTsBas() {
        intygTyp.value("string:ts-bas")
    }
    def valjIntygstypTsDiabetes() {
        intygTyp.value("string:ts-diabetes")
    }
    
    def valjIntygsTyp(String typ) {
        if (typ == "FK7263") {
            valjIntygstypFk7263();
        } else if (typ == "ts-bas") {
            valjIntygstypTsBas();
        } else if (typ == "ts-diabetes") {
            valjIntygstypTsDiabetes();
        }
        fortsattKnapp.click();
        waitFor {
            doneLoading()
        }
    }

}