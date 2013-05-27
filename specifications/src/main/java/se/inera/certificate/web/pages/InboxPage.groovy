package se.inera.certificate.web.pages

import geb.Page

class InboxPage extends Page {

	static url = "start#/lista"
 
	static at = { $("#inboxHeader").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
        archiveCertificateButton(required: false) { $("#archiveCertificateBtn") }
        viewCertificateButton(required: false) { $("#viewCertificateBtn") }
    }

    def archiveCertificate(String id) {
        $("#certificate-${id}").click();
        archiveCertificateButton.click();
    }

    def boolean certificateExists(String id) {
        $("#certificate-${id}").isDisplayed();
    }

    def viewCertificate(String id) {
        $("#certificate-${id}").click();
        viewCertificateButton.click();
    }

}
