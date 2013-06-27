package se.inera.certificate.web.pages

import geb.Page

class SendCertificateFlowPage extends Page {

    static at = { $("#send-certificate-flow-root").isDisplayed() }

    static content = {
        confirmAndSendBtn(required: false)  { $("#confirmAndSendBtn") }
        resultMessageContainer(required: false)  { $("#send-certificate-flow-result") }
        backToViewCertificateBtn(required: false)  { $("#backToViewCertificateBtn") }
        alreadySentWarningMessage(required: false)  { $("#already-sent-to-recipient-message") }
    }

    def confirmSendFlow() {
        confirmAndSendBtn.click()
    }
    
    def backToCertificateView() {
        backToViewCertificateBtn.click()
    }
}
