package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class ConsentPage extends AbstractPage {

    static at = { doneLoading() && consentTerms.isDisplayed() }

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
