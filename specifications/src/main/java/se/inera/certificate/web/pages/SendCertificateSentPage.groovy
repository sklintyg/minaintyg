package se.inera.certificate.web.pages

import geb.Page

class SendCertificateSentPage extends Page {

    static at = { $("#send-certificate-flow-root-sent").isDisplayed() }

    static content = {
        resultMessageContainer(required: false)  { $("#send-certificate-flow-result") }
        backToViewCertificateBtn(required: false)  { $("#backToViewCertificateBtn") }
    }


    def backToCertificateView() {
        backToViewCertificateBtn.click()
    }
}
