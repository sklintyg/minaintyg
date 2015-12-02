package se.inera.intyg.specifications.pages


class SendCertificateChooseRecipientPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#send-certificate-flow-root-recipients").isDisplayed() }

    static content = {
        chooseRecipientBtn(required: false)  { id -> $("recipient-${id}") }
        confirmRecipientSelectionBtn(required: false, to: SendCertificateSummaryPage, toWait: true) { $("#confirmRecipientSelectionBtn") }
        cancelRecipientSelectionBtn(required: false, to: IntygPage, toWait: true)  { $("#cancelRecipientSelectionBtn") }
        alreadySentWarningMessage(required: false) { $("#already-sent-to-recipient-message") }
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
