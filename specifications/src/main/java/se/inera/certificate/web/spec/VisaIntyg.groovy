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
        def result
        Browser.drive {
           result = $("#certType", title: typ).size() == 1
        }
        result
    }

    public boolean intygetsIdÄr(String id) {
        def result
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
        Browser.drive {
            return $("#lastest-certificate-event", text: contains(textFragment)).size() == 1
        }
    }
}
