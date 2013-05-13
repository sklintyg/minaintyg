package se.inera.certificate.web.spec

import geb.Browser
import se.inera.certificate.web.pages.ConsentDeniedPage
import se.inera.certificate.web.pages.ConsentPage
import se.inera.certificate.web.pages.InboxPage

public class ListaIntyg {

	public void loggaPåSom(String pnr) {
		Browser.drive {
			go "sso?guid=${pnr}"
		}
	}
	
	public boolean inkorgSidanVisas() {
		Browser.drive {
			at(InboxPage)
		}
	}
	
	public boolean listaMedIntygVisas() {
		Browser.drive {
            at(InboxPage)
			assert page.certificateTable.isDisplayed()
		}
	}
	
	public boolean finnsIngaIntyg() {
		Browser.drive {
            at(InboxPage)
            assert page.noCertificates.isDisplayed()
		}
	}
	
}
