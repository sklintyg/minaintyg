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
        Browser.drive {
           return $("#certType", title: typ).size() == 1
        }
    }

    public boolean intygetsIdÄr(String id) {
        Browser.drive {
            return $("#certId", title: id).size() == 1
        }
    }

    public void väljSkickaIntyg() {
        Browser.drive {
            page.startSendFlow()
        }
    }

    public boolean intygetHarEnStatusTextInnehållande(String textFragment) {
        Browser.drive {
            return $("#lastest-certificate-event", text: contains(textFragment)).size() == 1
        }
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
}
