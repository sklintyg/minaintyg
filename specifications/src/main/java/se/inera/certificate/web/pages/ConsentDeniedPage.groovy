package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class ConsentDeniedPage extends AbstractPage {

    static at = { doneLoading() && $("#refuseConsentMessage").isDisplayed() }
}
