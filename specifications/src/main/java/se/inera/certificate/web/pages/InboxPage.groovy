package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class InboxPage extends AbstractPage {

    static url = "start#/lista"

    static at = { doneLoading() && $("#inboxHeader").isDisplayed() }

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
        complementaryInfoText(required: false) { id -> $("#certificate-period-${id}")}
    }

    def archiveCertificate(String id) {
        archiveCertificateButton(id).click()
    }

    def confirmArchiveCertificate() {
        waitFor {
            doneLoading()
        }
        confirmArchiveButton.click()
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

    def String complementaryInfoIsSet(String id) {
        complementaryInfoText(id).text()
    } 
}
