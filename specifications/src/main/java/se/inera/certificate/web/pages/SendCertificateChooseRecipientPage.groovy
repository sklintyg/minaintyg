package se.inera.certificate.web.pages


class SendCertificateChooseRecipientPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#send-certificate-flow-root-recipients").isDisplayed() }

    static content = {
        chooseRecipientBtn(required: false)  { id -> $("recipient-${id}") }
        confirmRecipientSelectionBtn(required: false,wait: true) { displayed($("#confirmRecipientSelectionBtn")) }
        cancelRecipientSelectionBtn(required: false)  { $("#cancelRecipientSelectionBtn") }
        alreadySentWarningMessage(required: false,wait: true) { displayed($("#already-sent-to-recipient-message")) }
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
