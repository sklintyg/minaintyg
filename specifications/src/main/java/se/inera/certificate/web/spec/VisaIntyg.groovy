package se.inera.certificate.web.spec

import se.inera.certificate.web.pages.IntygPage

public class VisaIntyg {

    public boolean intygssidanVisas() {
        Browser.drive {
            waitFor {
                at IntygPage
            }
        }
    }

    public boolean intygetsTypÄr(String typ) {
        boolean result
        Browser.drive {
           result = $("#certType", title: typ).size() == 1
        }
        result
    }

    public boolean intygetsIdÄr(String id) {
        boolean result
        Browser.drive {
            result = $("#certId", title: id).size() == 1
        }
        result
    }

    public void väljSkickaIntyg() {
        Browser.drive {
            page.startSendFlow()
        }
    }

    public void väljArkiveraIntyg() {
        Browser.drive {
            page.archive();
        }
    }

    public void konfirmeraArkiveraIntyg() {
        Browser.drive {
            waitFor {
                confirmArchiveBtn.displayed
            }
            page.confirmArchiveCertificate()
        }
    }

    public boolean intygetHarEnStatusTextInnehållande(String textFragment) {
        boolean result
        Browser.drive {
            result = $("#latest-certificate-event", text: contains(textFragment)).size() == 1
        }
        result
    }

    public boolean intygetsDiagnoskodInnehåller(String kod) {
        def kod1
        Browser.drive {
            kod1 = page.diagnosKod
        }
        return kod1?.contains(kod)
    }
    public boolean intygetsDiagnosBeskrivningInnehåller(String text) {
        def text3
        Browser.drive {
            text3 = page.diagnosBeskrivning
        }
        return text3?.contains(text)
    }
        
    public boolean intygetsFält6aInnehållerKontaktMedArbetsförmedlingen() {
        def falt6aArbetsformedlingen
        Browser.drive {
            falt6aArbetsformedlingen = page.falt6aArbetsformedlingen
        }
        return falt6aArbetsformedlingen != null
    }

    public boolean intygetsFält6aInnehållerKontaktMedFöretagshälsovården() {
        def falt6aForetagshalsovarden
        Browser.drive {
            falt6aForetagshalsovarden = page.falt6aForetagshalsovarden
        }
        return falt6aForetagshalsovarden != null
    }

    public boolean intygetsFält6aÖvrigtInnehåller(String textFragment) {
        def falt6aOvrigt
        Browser.drive {
            falt6aOvrigt = page.falt6aOvrigt
        }
        return falt6aOvrigt?.contains(textFragment)

    }

    public boolean intygetsFält8bInnehållerNedsattMed25Procent() {
        def falt8bNedsatt25
        Browser.drive {
            falt8bNedsatt25 = page.falt8bNedsatt25
        }
        return falt8bNedsatt25 != null
    }
}
