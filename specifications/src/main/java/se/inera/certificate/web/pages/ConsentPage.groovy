package se.inera.certificate.web.pages

import geb.Page

class ConsentPage extends Page {

    static at = { $("#consentTerms").isDisplayed() }

    static content = {
        giveConsentButton { $("#giveConsentButton") }
        consentGivenView(required: false) { $("#consent-given") }
        continueToMIButton(required: false) { $("#continueToMI") }
    }

    def giveConsent() {
        giveConsentButton.click()
    }

    def continueToMI() {
        continueToMIButton.click()
    }
}
