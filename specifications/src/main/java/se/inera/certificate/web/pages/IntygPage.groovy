package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class IntygPage extends AbstractPage {

    static at = { doneLoading() && $("#certificate").isDisplayed() }

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
    }

    def confirmArchiveCertificate() {
        waitFor {
            doneLoading()
        }
        confirmArchiveBtn.click()
    }
}
