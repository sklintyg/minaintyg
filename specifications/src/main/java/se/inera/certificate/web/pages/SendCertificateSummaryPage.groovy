package se.inera.certificate.web.pages

import geb.Page

class SendCertificateSummaryPage extends Page {

    static at = { $("#send-certificate-flow-root").isDisplayed() }

    static content = {
        confirmAndSendBtn(required: false)  { $("#confirmAndSendBtn") }
        alreadySentWarningMessage(required: false)  { $("#already-sent-to-recipient-message") }
    }

    def confirmSendFlow() {
        confirmAndSendBtn.click()
    }
    
}
