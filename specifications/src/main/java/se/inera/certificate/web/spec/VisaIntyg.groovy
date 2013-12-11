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
}
