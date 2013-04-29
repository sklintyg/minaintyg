package se.inera.certificate.web.pages

import geb.Page

class ConsentDeniedPage extends Page {

	static at = { $("#refuseConsentMessage").isDisplayed() }

}
