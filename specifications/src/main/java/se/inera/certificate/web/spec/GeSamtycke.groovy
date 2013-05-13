package se.inera.certificate.web.spec;

import geb.Browser

import se.inera.certificate.web.pages.InboxPage
import se.inera.certificate.web.pages.ConsentPage
import se.inera.certificate.web.pages.ConsentDeniedPage

public class GeSamtycke {

	public void loggaPåSom(String pnr) {
		Browser.drive {
			go "sso?guid=${pnr}"
		}
	}
	
	public void geSamtycke() {
		Browser.drive {
			assert at(ConsentPage)
			page.giveConsent()
		}
	}
	
	public void nekaSamtycke() {
		Browser.drive {
			assert at(ConsentPage)
			page.denyConsent()
		}
	}
	
	public boolean inkorgSidanVisas() {
		Browser.drive {
			at(InboxPage)
		}
	}
	
	public boolean geSamtyckeSidanVisas() {
		Browser.drive {
			at(ConsentPage)
		}
	}
	
	public boolean samtyckeNekatSidanVisas() {
		Browser.drive {
			at(ConsentDeniedPage)
		}
	}
	
}
