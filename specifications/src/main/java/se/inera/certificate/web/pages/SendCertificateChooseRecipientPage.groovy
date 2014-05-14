package se.inera.certificate.web.pages

import geb.Page

class SendCertificateChooseRecipientPage extends Page {

    static at = { $("#send-certificate-flow-root-recipients").isDisplayed() }

    static content = {
        chooseRecipientBtn(required: false)  { id -> $("recipient-${id}") }
        confirmRecipientSelectionBtn(required: false)  { $("#confirmRecipientSelectionBtn") }
        cancelRecipientSelectionBtn(required: false)  { $("#cancelRecipientSelectionBtn") }
        alreadySentWarningMessage(required: false)  { $("#already-sent-to-recipient-message") }
    }

    def chooseRecipient(String id) {
        chooseRecipientBtn(id).click()
    }

    def confirmRecipientSelection() {
        confirmRecipientSelectionBtn.click()
    }

    def cancelRecipientSelection() {
        cancelRecipientSelectionBtn.click()
    }
}
