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

import org.codehaus.groovy.runtime.StackTraceUtils

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.webcert.specifications.pages.fk7263.EditeraFk7263Page
import se.inera.intyg.webcert.specifications.spec.util.screenshot.ExceptionHandlingFixture

class VerifieraUtkastFk7263 extends ExceptionHandlingFixture {
    public VerifieraUtkastFk7263() {
    }

    String getCurrentMethodName(){
        def marker = new Throwable()
        return StackTraceUtils.sanitize(marker).stackTrace[1].methodName
    }

    String getStringResult(module, field) {
        def result = ''
        if (module != null) {
            Browser.drive {
                if (!page."$module"."$field".isDisplayed()) {
                    result = "notshown"
                } else {
                    result = page."$module"."$field".text()
                }
            }
        } else {
            Browser.drive {
                if (!page."$field".isDisplayed()) {
                    result = "notshown"
                } else {
                    result = page."$field".text()
                }
            }
        }
        result
    }
    
    String getValueResult(module, field) {
        def result = ''
        if (module != null) {
            Browser.drive {
                if (!page."$module"."$field".isDisplayed()) {
                    result = "notshown"
                } else {
                    result = page."$module"."$field".value()
                }
            }
        } else {
            Browser.drive {
                if (!page."$field".isDisplayed()) {
                    result = "notshown"
                } else {
                    result = page."$field".value()
                }
            }
        }
        result
    }

    String getBoxResult(module, field) {
        def result = ''
        if (module != null) {
            Browser.drive {
                result = page."$module"."$field"
            }
        } else {
            Browser.drive {
                result = page."$field"
            }
        }
        result
    }


    boolean getBooleanResult(field) {
        def result = false
        Browser.drive {
            result = page."$field".isDisplayed()
        }
        result
    }

    // --- form 2
    String diagnos1() {
        getValueResult("diagnos", getCurrentMethodName())
    }
    
    String diagnos1Text() {
        getValueResult("diagnos", getCurrentMethodName())
    }
    String diagnos2() {
        getValueResult("diagnos", getCurrentMethodName())
    }
    String diagnos2Text() {
        getValueResult("diagnos", getCurrentMethodName())
    }
    String diagnos3() {
        getValueResult("diagnos", getCurrentMethodName())
    }
    String diagnos3Text() {
        getValueResult("diagnos", getCurrentMethodName())
    }
    String fortydligande() {
        getValueResult("diagnos", getCurrentMethodName())
    }

    // --- form 3
    String sjukdomsforlopp() {
        getValueResult(null, getCurrentMethodName())
    }

    // --- form 4
    String funktionsnedsattning() {
        getValueResult(null, getCurrentMethodName())
    }

    // --- form 4b
    boolean other() {
        getValueResult("baserasPa", getCurrentMethodName())
    }

    String otherText() {
        getValueResult("baserasPa", getCurrentMethodName())
    }

    boolean journal() {
        getValueResult("baserasPa", getCurrentMethodName())
    }

    boolean telefonkontakt() {
        getValueResult("baserasPa", getCurrentMethodName())
    }

    boolean undersokning() {
        getValueResult("baserasPa", getCurrentMethodName())
    }

    // --- form 5
    String aktivitetsbegransning() {
        getValueResult(null, getCurrentMethodName())
    }

    // --- form 8a
    boolean arbetslos() {
        getValueResult("arbete", getCurrentMethodName())
    }

    boolean foraldraledig() {
        getValueResult("arbete", getCurrentMethodName())
    }

    boolean nuvarande() {
        getValueResult("arbete", getCurrentMethodName())
    }

    String arbetsuppgifter() {
        getValueResult("arbete", getCurrentMethodName())
    }

    // --- form 6a
    boolean rekommendationKontaktAf() {
        getValueResult("rekommendationer", "kontaktAf")
    }

    boolean rekommendationKontaktForetagshalsovard() {
        getValueResult("rekommendationer", "kontaktForetagshalsovard")
    }

    boolean rekommendationOvrigt() {
        getValueResult("rekommendationer", "ovrigt")
    }

    String rekommendationOvrigtBeskrivning() {
        getValueResult("rekommendationer", "ovrigtBeskrivning")
    }

    // --- form 7
    String arbetslivsinriktadRehabilitering() {
        getBoxResult("rekommendationer", "radioGroupRehab")
    }

    String recommendationsToFkTravel() {
        getBoxResult("rekommendationer", "radioGroupResor")
    }

    // --- form 11
    String recommendationsToFkReabInQuestion() {
        getValueResult("rekommendationer", getCurrentMethodName())
    }

    // --- form 10
    String prognos() {
        getBoxResult("prognos", "radioGroup")
    }

    String prognosBeskrivning() {
        getValueResult("prognos", "beskrivning")
    }
}
