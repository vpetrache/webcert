package se.inera.webcert.spec.web.pages.fk7264

import geb.Page

class ViewCertQAPage extends Page {

    static at = { $("#viewCertAndQA").isDisplayed() }

    static content = {
        unhandledQAList(required: false) { $("#unhandledQACol") }
        askQuestionBtn(required: false) { $("#askQuestionBtn") }
        certificateRevokedMessage(required: false) { $("#certificate-is-revoked-message-text") }
        certificateIsSentToFKMessage(required: false) { $("#certificate-is-sent-to-fk-message-text") }
        certificateIsSentToITMessage(required: false) { $("#certificate-is-sent-to-it-message-text") }
        
        newQuestionForm { $("#newQuestionForm") }
        newQuestionText { $("#newQuestionText") }
        sendQuestionBtn { $("#sendQuestionBtn") }
        cancelQuestionBtn { $("#cancelQuestionBtn") }
        selectSubjectListbox { $("#new-question-topic") }

        field1yes{$("#field1yes")}
        field1no{$("#field1no")}
        field2{$("#field2")}
        field3{$("#field3")}
        field4{$("#field4")}
        field4b{$("#field4b")}
        field5{$("#field5")}
        field6a{$("#field6a")}
        field6b{$("#field6b")}
        field7{$("#field7")}
        field8a{$("#field8")}
        field8b{$("#field8b")}
        field9{$("#field9")}
        field10{$("#field10")}
        field11{$("#field11")}
        field12{$("#field12")}
        field13{$("#field13")}
        field17{$("#field17")}
        field_vardperson_namn{$("#vardperson_namn")}
        field_vardperson_enhetsnamn{$("#vardperson_enhetsnamn")}


    }

    def addAnswerText(String internid, String answer){
        $("#answerText-${internid}")<< answer
    }

    def answerBtn(String internid) {
        $("#sendAnswerBtn-${internid}")
    }
    
    def frageStallarNamn(String internid) {
        $("#fraga-vard-aktor-namn-${internid}")
    }
    def besvarareNamn(String internid) {
        $("#svar-vard-aktor-namn-${internid}")
    }
    
    def markAsHandledFkOriginBtn(String internid) {
        $("#markAsHandledFkOriginBtn-${internid}")
    }
    def markAsHandledWcOriginBtn(String internid) {
        $("#markAsHandledWcOriginBtn-${internid}")
    }
    def qaHandledPanel(String internid) {
        $("#qahandled-${internid}")
    }
    def qaUnhandledPanel(String internid) {
        $("#qaunhandled-${internid}")
    }

    def markAsUnhandledBtn(String internid) {
        $("#markAsUnhandledBtn-${internid}")
    }

    def sendAnswer(String internid) {
        $("#sendAnswerBtn-${internid}").click()
    }

    def initQuestion(){
        $("#askQuestionBtn").click()
    }
    
    def addQuestionText( String question){
        newQuestionText<< question
    }

    def selectSubject(String amne) {
        selectSubjectListbox = amne
    }
    def sendQuestion() {
        sendQuestionBtn.click()
    }

    def cancelQuestion() {
        cancelQuestionBtn.click()
    }

    def fkMeddelandeRubrik(String internid) {
        $("#fkMeddelandeRubrik-${internid}").click()
    }
    def fkKompletteringar(String internid) {
        $("#fkKompletteringar-${internid}").click()
    }
    def fkKontakter(String internid) {
        $("#fkKontakter-${internid}").click()
    }

    def qaFragaSkickadDatum(String internid) {
        $("#qa-skickaddatum-${internid}")
    }

    def qaFragetext(String internid) {
        $("#qa-fragetext-${internid}")
    }

    def qaSvarstext(String internid) {
        $("#answerText-${internid}")

        //$("qaHandledSvarstext-#${internid}")
    }



}
