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
    }

    def restoreCertificate(String id) {
        $("#restoreCertificate-${id}").click()
    }

    def boolean certificateExists(String id) {
        System.out.println("HAPP")
        $("#restoreCertificate-${id}").isDisplayed()
    }

    def goToInboxPage() {
        inboxTab.click()
    }
}
