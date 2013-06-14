package se.inera.certificate.web.pages

import geb.Page

class ConsentPage extends Page {

    static at = { consentTerms.isDisplayed() }

    static content = {
        consentTerms { $("#consentTerms") }
        giveConsentButton { $("#giveConsentButton") }
        consentGivenView(required: false) { $("#consent-given") }
        continueToMIButton(required: false) { $("#continueToMI") }
        backToMvkLink { $("#backToMvkLink") }
        logoutLink { $("#mvklogoutLink") }
    }

    def giveConsent() {
        giveConsentButton.click()
    }

    def continueToMI() {
        continueToMIButton.click()
    }

    def goBackToMvk() {
        backToMvkLink.click()
    }

    def logout() {
        logoutLink.click()
    }
}
