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
        certificate(required: false) { id -> $("#certificate-${id}") }
        archiveCertificateButton(required: false) { id -> $("#archiveCertificateBtn-${id}") }
        viewCertificateButton(required: false) { id -> $("#viewCertificateBtn-${id}") }
    }

    def archiveCertificate(String id) {
        archiveCertificateButton(id).click()

        // Since this dialog is animated, wait for the same time as the animation
        // in order to avoid problem with Chrome WebDriver and moving click targets
        Thread.sleep(300);
    }

    def confirmArchiveCertificate() {
        confirmArchiveButton.click()

        // Since this dialog is animated, wait for the same time as the animation
        // in order to avoid problem with Chrome WebDriver and moving click targets
        Thread.sleep(300);
    }

    def boolean certificateExists(String id) {
        certificate(id).isDisplayed();
    }

    def viewCertificate(String id) {
        viewCertificateButton(id).click()
    }

    def goToArchivedTab() {

        archivedTab.click()
    }

    def goToAboutMinaIntyg() {
        aboutTab.click()
    }

    def boolean cancelledCertificateDisplayed(String id) {
        def viewCertButton = viewCertificateButton(id)
        !viewCertButton.isDisplayed() || viewCertButton.isDisabled();
    }
}
