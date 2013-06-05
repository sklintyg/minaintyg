package se.inera.certificate.web.pages

import geb.Page

class InboxPage extends Page {

    static url = "start#/lista"

    static at = { $("#inboxHeader").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
        inboxTab(required: false) { $("#inboxTab") }
        archivedTab(required: false) { $("#archivedTab") }
        aboutTab(required: false) { $("#aboutTab") }
    }

    def archiveCertificate(String id) {
        $("#archiveCertificateBtn-${id}").click()
    }

    def boolean certificateExists(String id) {
        $("#certificate-${id}").isDisplayed();
    }

    def viewCertificate(String id) {
        $("#viewCertificateBtn-${id}").click()
    }

    def goToArchivedTab() {
        archivedTab.click()
    }
    
    def goToAboutMinaIntyg() {
        aboutTab.click()
    }
}
