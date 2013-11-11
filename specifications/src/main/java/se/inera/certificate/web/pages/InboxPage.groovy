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
        confirmArchiveButton(required: false) { $("#archive-button") }
    }

    def archiveCertificate(String id) {
        $("#archiveCertificateBtn-${id}").click()
    }

    def confirmArchiveCertificate() {
        // Since this dialog is animated, wait for the same time as the animation
        // in order to avoid problem with Chrome WebDriver and moving click targets
        Thread.sleep(300);
        confirmArchiveButton.click()
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

    def boolean cancelledCertificateDisplayed(String id) {
        $("#viewCertificateBtn-${id}").isDisabled();
    }
}
