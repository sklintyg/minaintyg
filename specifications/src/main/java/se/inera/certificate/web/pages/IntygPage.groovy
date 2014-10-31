package se.inera.certificate.web.pages

import geb.Page

class IntygPage extends Page {

    static at = { $("#certificate").isDisplayed() }

    static content = {
        certificateId { $("#certId") }
        certificateType { $("#certType") }
        sendCertificateBtn { $("#sendCertificateBtn") }
        latestCertificateEvent { $("#latest-certificate-event") }
        archiveBtn { $("#archiveBtn") }
        confirmArchiveBtn(required: false) { $("#archive-button") }
        diagnosKod1(required: false) { $("#diagnosKod1").text() }
        diagnosKlartext1(required: false) { $("#diagnosKlartext1").text() }
        diagnosKod2(required: false) { $("#diagnosKod2").text() }
        diagnosKlartext2(required: false) { $("#diagnosKlartext2").text() }
        diagnosKod3(required: false) { $("#diagnosKod3").text() }
        diagnosKlartext3(required: false) { $("#diagnosKlartext3").text() }
        falt6aArbetsformedlingen(required: false) { $("span", text: contains("Kontakt med Arbetsförmedlingen")).text() }
        falt6aForetagshalsovarden(required: false) { $("span", text: contains("Kontakt med företagshälsovården")).text() }
        falt6aOvrigt(required: false) { $("span", text: contains("Övrigt:")).text() }
        falt8bNedsatt25 { $("#nedsattMed25") }
    }

    def startSendFlow() {
        sendCertificateBtn.click()
    }

    def archive() {
        archiveBtn.click()

        // Since this dialog is animated, wait for the same time as the animation
        // in order to avoid problem with Chrome WebDriver and moving click targets
        Thread.sleep(300);
    }

    def confirmArchiveCertificate() {
        confirmArchiveBtn.click()

        // Since this dialog is animated, wait for the same time as the animation
        // in order to avoid problem with Chrome WebDriver and moving click targets
        Thread.sleep(500);
    }
}
