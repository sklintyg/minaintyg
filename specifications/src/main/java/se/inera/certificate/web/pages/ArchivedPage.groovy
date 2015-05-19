package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class ArchivedPage extends AbstractPage {

	static url = "start/#/arkiverade"

	static at = {doneLoading() && $("#archivedHeader").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
        archiveCertificateButton(required: false) { $("#archiveCertificateBtn") }
        viewCertificateButton(required: false) { $("#viewCertificateBtn") }
        inboxTab(required: false) { $("#inboxTab") }
        archivedTab(required: false) { $("#archivedTab") }
        confirmRestoreButton(required: false) { $("#restore-button") }
        restoreCertificateButton(required: false) { id -> $("#restoreCertificate-${id}") }
    }

    def restoreCertificate(String id) {
        restoreCertificateButton(id).click()
    }

    def confirmRestoreCertificate() {
        waitFor {
            doneLoading()
        }
        confirmRestoreButton.click()
    }

    def boolean certificateExists(String id) {
        restoreCertificateButton(id).isDisplayed()
    }

    def goToInboxPage() {
        AbstractPage.scrollIntoView("inboxTab")
        waitFor {
            inboxTab.click()
        }
    }
}
