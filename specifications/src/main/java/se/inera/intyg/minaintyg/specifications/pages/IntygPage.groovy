package se.inera.intyg.minaintyg.specifications.pages


class IntygPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#certificate").isDisplayed() }

    static content = {
        certificateId { $("#certId") }
        certificateType { $("#certType") }
        sendCertificateBtn(to: SendCertificateChooseRecipientPage, toWait: true) { $("#sendCertificateBtn") }
        latestCertificateEvent { $("#latest-certificate-event") }
        archiveBtn { $("#archiveBtn") }
        confirmArchiveBtn(required: false) { $("#archive-button") }
    }

    def startSendFlow() {
        sendCertificateBtn.click()
    }

    def archive() {
        archiveBtn.click()
        waitFor {
            doneLoading()
        }
    }

    def confirmArchiveCertificate() {
        confirmArchiveBtn.click()
        // TODO: FIX!! The animation on InboxPage requires delay, otherwise doneLoading() returns true immediately
        Thread.sleep(1000)
        waitFor {
            doneLoading()
        }
    }
    
    boolean hasStatus(String status) {
        latestCertificateEvent.any { it.text().contains(status) }
    }
}
