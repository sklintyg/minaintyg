package se.inera.certificate.web.pages

import geb.Page

class IntygPage extends Page {

    static at = { $("#certificate").isDisplayed() }

    static content = {
        certificateId { $("#certId") }
        certificateType { $("#certType") }
        sendCertificateBtn { $("#sendCertificateBtn") }
        falt6aArbetsformedlingen(required: false) { $("span", text: contains("Kontakt med Arbetsförmedlingen")).text() }
        falt6aForetagshalsovarden(required: false) { $("span", text: contains("Kontakt med företagshälsovården")).text() }
        falt6aOvrigt(required: false) { $("span", text: contains("Övrigt:")).text() }
    }

    def startSendFlow() {
        sendCertificateBtn.click()
    }
}
