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

package se.inera.intyg.webcert.specifications.pages

import se.inera.intyg.webcert.specifications.spec.Browser

class VisaFragaSvarPage extends AbstractViewCertPage {

    static at = { doneLoading() && $("#viewQAAndCert").isDisplayed() }

    static content = {

        intygVy(wait: true) { $('#intyg-vy-laddad') }

        // Buttons
        newQuestionBtn(required: false, wait: true) { $("#askArendeBtn") }
        newQuestionBtnNoWait(required: false) { $("#askArendeBtn") }
        newQuestionForm(required: false) { $("#arendeNewModelForm") }
        newQuestionFormNoWait(required: false) {$("#arendeNewModelForm") }
        newQuestionText { $("#arendeNewModelText") }
        newQuestionTopic { $("#new-question-topic") }

        sendQuestionBtn(required: false, wait: true) { $("#sendArendeBtn") }
        cancelQuestionBtn { $("#cancelArendeBtn") }
        makuleraButton { $("#makuleraBtn") }

        skrivUtBtn(required: false, wait: true) { $("#downloadprint") }
        skrivUtBtnEmployer(required: false, wait: true) { $("#downloadprintemployer") }

        copyButton(wait: true) { $("#copyBtn") }
        kopieraBtn(required: false, wait: true) { $("#copyBtn") }
        kopieraBtnNoWait(required: false) { $("#copyBtn") }

        makuleraBtn(required: false, wait: true) { $("#makuleraBtn") }
        makuleraBtnNoWait(required: false) { $("#makuleraBtn") }

        skickaTillFkBtn(required: false, wait: true) { $("#sendBtn") }
        skickaTillFkBtnNoWait(required: false) { $("#sendBtn") }

        // Dialogs
        qaOnlyDialog(required: false, wait: true) { $("#qa-only-warning-dialog") }
        qaOnlyDialogFortsatt(required: false, wait: true) { $("#button1continue-dialog") }
        qaOnlyDialogCancel(required: false) { $("#button2qa-only-warning-dialog") }

        unhandledQAList(wait: true) { $("#unhandledArendeCol") }

        questionIsSentToFkMessage(required: false, wait: true) { $("#arende-is-sent-to-fk-message-text") }
        questionIsSentToFkMessageNoWait(required: false) { $("#arende-is-sent-to-fk-message-text") }

        closeSentMessage(wait: true) { $("#arende-is-sent-to-fk-message-text > button") }

        certificateRevokedMessage(required: false, wait: true) { $("#certificate-is-revoked-message-text") }
        certificateIsSentToFKMessage(required: false, wait: true) { $("#intyg-is-sent-to-fk-message-text") }
        certificateIsSentToFKMessageNoWait(required: false) { $("#intyg-is-sent-to-fk-message-text") }
        certificateIsNotSentToFkMessage(required: false) { $("#intyg-is-not-sent-to-fk-message-text") }

        kopieraDialogKopieraKnapp { $("#button1copy-dialog") }
        makuleraDialogMakuleraKnapp { $("#button1makulera-dialog") }
        makuleraConfirmationOkButton(wait: true) { $("#confirmationOkButton") }
        skickaDialogSkickaKnapp { $("#button1send-dialog") }

        tillbakaButton(wait: true) { $("#tillbakaButton") }

        hanteraButton(wait: true) { $("#button1checkhanterad-dialog-hantera") }
        ejHanteraButton(wait: true) { $("#button1checkhanterad-dialog-ejhantera") }
        hanteraTillbakaButton(wait: true) { $("#button1checkhanterad-dialog-tillbaka") }

        qaCheckEjHanteradDialog(wait: true) { $("#qa-check-hanterad-dialog") }

        linkEjSigneradeUtkast(required: false) { $("#menu-unsigned") }
        linkSokSkrivIntyg(required: false) { $("#menu-skrivintyg") }

        vidarebefordradKnappPaFraga(required: false) { $("#unhandled-vidarebefordraEjHanterad") }

        svaraEjMojligtDialog(required: false) { $('#answerDisabledReasonPanel') }
    }

    def copy() {
        $("#copyBtn").click()
        waitFor {
            doneLoading()
        }
        kopieraDialogKopieraKnapp.click()
    }

    def makulera() {
        $("#makuleraBtn").click()
        waitFor {
            doneLoading()
        }
        makuleraDialogMakuleraKnapp.click()
    }

    def showNewQuestionForm() {
        newQuestionBtn.click()
    }

    def sendQuestion() {
        sendQuestionBtn.click()
    }

    def cancelQuestion() {
        cancelQuestionBtn.click()
    }

    def answerBtn(String internid) {
        Browser.drive {
            waitFor {
                $("#sendAnswerBtn-${internid}")
            }
        }
    }

    def forwardBtn(String internid) {
        Browser.drive {
            waitFor {
                $("#forwardBtn-${internid}")
            }
        }
    }

    def frageStallarNamn(String internid) {

        Browser.drive {
            waitFor {
                $("#fraga-vard-aktor-namn-${internid}")
            }
        }
    }

    def besvarareNamn(String internid) {
        Browser.drive {
            waitFor {
                $("#svar-vard-aktor-namn-${internid}")
            }
        }
    }

    def markAsHandledFkOriginBtn(String internid) {
        Browser.drive {
            waitFor {
                $("#markAsHandledFkOriginBtn-${internid}")
            }
        }
    }

    def markAsHandledWcOriginBtnClick(String id) {
        Browser.drive {
            waitFor {
                $("#markAsHandledWcOriginBtn-${id}").click()
            }
        }
    }

    boolean qaHandledPanel(String internid) {
        def ref = "#arende-handled-${internid}";
        def result
        Browser.drive {
                result = $(ref).isDisplayed()
        }
        return result
    }

    boolean qaUnhandledPanel(String id) {
        def ref = "#arende-unhandled-${id}";
        def result
        Browser.drive {
                result = $(ref).isDisplayed()
        }
        return result
    }

    def qaUnhandledPanelWithText(String text) {
        Browser.drive {
            waitFor {
                $("#unhandledQACol div", text: text)
            }
        }
    }

}

