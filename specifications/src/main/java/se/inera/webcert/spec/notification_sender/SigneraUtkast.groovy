package se.inera.webcert.spec.notification_sender
import se.inera.webcert.spec.util.WebcertRestUtils

class SigneraUtkast {

    String intygId
    String intygTyp
    long   version

    def response

    public void execute() {
        WebcertRestUtils.login()
        response = WebcertRestUtils.signUtkast(intygTyp, intygId, version)
    }

    public boolean utkastSignerat() {
        response.success
    }

    public long version() {
        response.data.version
    }

}
