package se.inera.certificate.web.pages


class SendCertificateSentPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#send-certificate-flow-root-sent").isDisplayed() }

    static content = {
        resultMessageContainer(required: false,wait: true) { displayed($("#send-certificate-flow-result")) }
        backToViewCertificateBtn(required: false)  { $("#backToViewCertificateBtn") }
    }


    def backToCertificateView() {
        backToViewCertificateBtn.click()
    }
}
