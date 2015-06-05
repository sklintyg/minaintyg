package se.inera.certificate.web.pages


class IntygPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#certificate").isDisplayed() }

    static content = {
        certificateId { $("#certId") }
        certificateType { $("#certType") }
        sendCertificateBtn { $("#sendCertificateBtn") }
        latestCertificateEvent { $("#latest-certificate-event") }
        archiveBtn { $("#archiveBtn") }
        confirmArchiveBtn(required: false) { $("#archive-button") }
    }

    def startSendFlow() {
        sendCertificateBtn.click()
    }

    def archive() {
        archiveBtn.click()
    }

    def confirmArchiveCertificate() {
        waitFor {
            doneLoading()
        }
        confirmArchiveBtn.click()
    }
}
