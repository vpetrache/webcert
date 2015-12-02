package se.inera.intyg.webcert.specifications.spec.web

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.webcert.specifications.pages.AccessDeniedPage
import se.inera.intyg.webcert.specifications.pages.SokSkrivaIntygPage
import se.inera.intyg.webcert.specifications.pages.UnhandledQAPage
import se.inera.intyg.webcert.specifications.pages.UnsignedIntygPage
import se.inera.intyg.webcert.specifications.spec.util.screenshot.ExceptionHandlingFixture

class Features extends ExceptionHandlingFixture {

    void gaTillSvaraOchFraga() {
        Browser.drive {
            to UnhandledQAPage
            waitFor {
                at UnhandledQAPage
            }
        }
    }

    void gaTillSokSkrivIntyg() {
        Browser.drive {
            to SokSkrivaIntygPage
            waitFor {
                at SokSkrivaIntygPage
            }
        }
    }

    void gaTillUnsignedIntyg() {
        Browser.drive {
            to UnsignedIntygPage
            waitFor {
                at UnsignedIntygPage
            }
        }
    }

    boolean accessDeniedSidanVisas() {
        boolean result
        Browser.drive {
            waitFor {
                result = isAt AccessDeniedPage
            }
        }
        result
    }

    boolean fragaSvarSynsIMenyn() {
        boolean result
        Browser.drive {
            waitFor {
                result = isAt(AbstractLoggedInPage) && page.unhandledQa.isDisplayed()
            }
        }
        result
    }

    boolean omWebcertSynsIMenyn() {
        boolean result
        Browser.drive {
            waitFor {
                result = isAt(AbstractLoggedInPage) && page.omWebcert.isDisplayed()
            }
        }
        result
    }

    boolean sokSkrivIntygSynsIMenyn() {
        boolean result
        Browser.drive {
            waitFor {
                result = isAt(AbstractLoggedInPage) && page.skrivIntyg.isDisplayed()
            }
        }
        result
    }

    boolean ejSigneradeUtkastSynsIMenyn() {
        boolean result
        Browser.drive {
            waitFor {
                result = isAt(AbstractLoggedInPage) && page.unsigned.isDisplayed()
            }
        }
        result
    }

    boolean personnummerSyns() {
        boolean result
        Browser.drive {
            result = page.$('#pnr')?.isDisplayed()
        }
        result
    }

    /*
    def restUtkast() {
        Browser.drive {
            go "/api/utkast"
            waitFor {
                page.$('body').text() == 'Not available since feature is not active'
            }
        }
        true
    }

    def restModuleUtkastGet() {
        Browser.drive {
            go "/moduleapi/utkast/fk7263/webcert-fitnesse-features-1"
            waitFor {
                page.$('body').text() == 'Not available since feature is not active'
            }
        }
        true
    }
    */

}