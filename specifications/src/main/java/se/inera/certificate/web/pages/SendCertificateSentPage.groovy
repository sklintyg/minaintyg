package se.inera.certificate.web.pages


class SendCertificateSentPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#send-certificate-flow-root-sent").isDisplayed() }

    static content = {
        resultMessageContainer(required: false) { $("#send-certificate-flow-result") }
        backToViewCertificateBtn(to: IntygPage, toWait: true)  { $("#backToViewCertificateBtn") }
    }


    def backToCertificateView() {
        backToViewCertificateBtn.click()
    }
}
