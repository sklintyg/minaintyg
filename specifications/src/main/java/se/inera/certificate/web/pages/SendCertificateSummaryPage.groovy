package se.inera.certificate.web.pages


class SendCertificateSummaryPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#send-certificate-flow-root-summary").isDisplayed() }

    static content = {
        confirmAndSendBtn(required: false, to: SendCertificateSentPage, toWait: true) { $("#confirmAndSendBtn") }
        backToViewRecipentsBtn(required: false, to: SendCertificateChooseRecipientPage, toWait: true)  { $("#backToViewRecipentsBtn") }
        cancelSendBtn(required: false, to: IntygPage, toWait: true)  { $("#cancelSendBtn") }
        selectedRecipientMessage(required: false)  { $("#selected-recipient-message") }
        alreadySentWarningMessage(required: false)  { $("#already-sent-to-recipient-message") }
    }

    def confirmSend() {
        confirmAndSendBtn.click()
    }

    def backToViewRecipents() {
        backToViewRecipentsBtn.click()
    }

    def cancelSend() {
        cancelSendBtn.click()
    }
}
