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

    public boolean intygetHarEnStatusTextInnehållande(String textFragment) {
        boolean result
        Browser.drive {
            result = $("#lastest-certificate-event", text: contains(textFragment)).size() == 1
        }
        result
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

    public boolean intygetsFält8bInnehållerNedsattMedProcent(int procent) {
        def falt8bNedsatt
        Browser.drive {
            falt8bNedsatt = page."falt8bNedsatt${procent}"
        }
        return falt8bNedsatt != null
    }

    public boolean intygetsFält8bNedsattMedProcentInnehåller(int procent, String textFragment) {
        def falt8bNedsatt
        Browser.drive {
            falt8bNedsatt = page."falt8bNedsatt${procent}"
        }
        return falt8bNedsatt?.text().contains(textFragment)
    }
}
