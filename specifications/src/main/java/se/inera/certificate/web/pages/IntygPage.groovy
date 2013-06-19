package se.inera.certificate.web.pages

import geb.Page

class IntygPage extends Page {

    static at = { $("#certificate").isDisplayed() }

    static content = {
        certificateId { $("#certId") }
        certificateType { $("#certType") }
        sendCertificateBtn { $("#sendCertificateBtn") }
    }

    def startSendFlow() {
        sendCertificateBtn.click()
    }
}
