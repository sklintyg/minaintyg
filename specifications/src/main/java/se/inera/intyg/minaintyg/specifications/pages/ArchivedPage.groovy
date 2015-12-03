package se.inera.intyg.minaintyg.specifications.pages

import se.inera.intyg.common.specifications.page.AbstractPage

class ArchivedPage extends AbstractLoggedInPage {

	static url = "start/#/arkiverade"

	static at = {doneLoading() && $("#archivedHeader").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
        archiveCertificateButton(required: false) { $("#archiveCertificateBtn") }
        viewCertificateButton(required: false) { $("#viewCertificateBtn") }
        confirmRestoreButton(required: false) { $("#restore-button") }
        restoreCertificateButton(required: false) { id -> $("#restoreCertificate-${id}") }
    }

    def restoreCertificate(String id) {
        restoreCertificateButton(id).click()
        waitFor {
            doneLoading()
        }
    }

    def confirmRestoreCertificate() {
        confirmRestoreButton.click()
        waitFor {
            doneLoading()
        }
    }

    def boolean certificateExists(String id) {
        restoreCertificateButton(id).isDisplayed()
    }
}
