package se.inera.certificate.web.pages

import geb.Page

class ArchivedPage extends Page {

	static url = "start#/arkiverade"
 
	static at = { $("#archivedHeader").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
        archiveCertificateButton(required: false) { $("#archiveCertificateBtn") }
        viewCertificateButton(required: false) { $("#viewCertificateBtn") }
        inboxTab(required: false) { $("#inboxTab") }
        archivedTab(required: false) { $("#archivedTab") }
        confirmRestoreButton(required: false) { $("#restore-button") }
    }

    def restoreCertificate(String id) {
        $("#restoreCertificate-${id}").click()
    }

    def confirmRestoreCertificate() {
        // Since this dialog is animated, wait for the same time as the animation
        // in order to avoid problem with Chrome WebDriver and moving click targets
        Thread.sleep(300);
        confirmRestoreButton.click()
    }

    def boolean certificateExists(String id) {
        $("#restoreCertificate-${id}").isDisplayed()
    }

    def goToInboxPage() {
        inboxTab.click()
    }
}
