package se.inera.certificate.web.spec

import geb.Browser
import se.inera.certificate.web.pages.IntygPage

public class VisaIntyg {

    public boolean intygssidanVisas() {
        Browser.drive {
            at(IntygPage)
        }
    }

    public boolean intygetsTypÄr(String typ) {
        def result = false
        Browser.drive {
            at(IntygPage)
            result = ($("#certType", title: typ).size() == 1)
        }
        result
    }

    public boolean intygetsIdÄr(String id) {
        def result = false
        Browser.drive {
            at(IntygPage)
            result = ($("#certId", title: id).size() == 1)
        }
        result
    }


}
