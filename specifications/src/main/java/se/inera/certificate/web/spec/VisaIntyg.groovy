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
        Browser.drive {
            at(IntygPage)
            assert $("#certType", title:typ).size() == 1
        }
    }

    public boolean intygetsIdÄr(String id) {
        Browser.drive {
            at(IntygPage)
            assert $("#certId", title:id).size() == 1
        }
    }


}
