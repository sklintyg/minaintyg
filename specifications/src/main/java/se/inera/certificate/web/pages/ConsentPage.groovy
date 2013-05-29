package se.inera.certificate.web.pages

import geb.Page

class ConsentPage extends Page {

    static at = { $("#consentTerms").isDisplayed() }

    static content = {
        giveConsentButton { $("#giveconsent") }
        denyConsentButton { $("#noconsentBtn") }
    }

    def giveConsent() {
        giveConsentButton.click()
    }

    def denyConsent() {
        denyConsentButton.click()
    }


}
