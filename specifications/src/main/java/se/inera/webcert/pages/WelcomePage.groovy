package se.inera.webcert.pages

import geb.Page

class WelcomePage extends Page {

    static at = { $("#loginForm").isDisplayed() }

    static content = {
        userSelect { $("#jsonSelect") }
        loginBtn { $("#loginBtn") }
    }

    def loginAs(String id) {
        System.err.println($("${id}"))
        System.err.println($("#${id}"))
        System.err.println($("#${id}").value())
        userSelect = $("#${id}").value();
        loginBtn.click()
    }
}
