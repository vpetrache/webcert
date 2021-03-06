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

package se.inera.intyg.webcert.specifications.spec

import geb.driver.CachingDriverFactory

import org.openqa.selenium.Cookie
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import se.inera.intyg.webcert.specifications.page.AbstractPage

public class Browser {

    private static geb.Browser browser

    static void öppna() {
        if (browser) throw new IllegalStateException("Browser already initialized")
        browser = new geb.Browser()
        browser.driver.manage().window().setSize(new Dimension(1280, 1024));
    }

    public void stäng() {
        if (!browser) throw new IllegalStateException("Browser not initialized")
        browser.quit()
        browser = null
        CachingDriverFactory.clearCache()
    }

    public void laddaOm() {
        if (!browser) throw new IllegalStateException("Browser not initialized")
        browser.driver.navigate().refresh()
        browser.drive {
            waitFor {
                AbstractPage.doneLoading()
            }
        }
    }

    static geb.Browser drive(Closure script) {
        if (!browser) throw new IllegalStateException("Browser not initialized")
        script.delegate = browser
        script()
        browser
    }

    static String getJSession() {
        browser.getDriver().manage().getCookieNamed("JSESSIONID").getValue()
    }

    static String getRouteId() {
        if (browser.getDriver().manage().getCookieNamed("ROUTEID") != null) {
              return browser.getDriver().manage().getCookieNamed("ROUTEID").getValue()
        }
        return ""
    }

    static String deleteCookie(cookieName) {
        Cookie cookie = new Cookie(cookieName, "")
        browser.getDriver().manage().deleteCookie(cookie)
    }

    static String setCookie(cookieName, cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue)
        browser.getDriver().manage().addCookie(cookie)
    }

    static String setLocalStorageValue(String item, String value) {
        ((JavascriptExecutor) browser.getDriver()).executeScript(String.format(
                "window.localStorage.setItem('%s','%s');", item, value));

    }

    static String getTitle() {
        browser.getDriver().getTitle()
    }

    static getDriver() {
        return browser.getDriver();
    }
}
