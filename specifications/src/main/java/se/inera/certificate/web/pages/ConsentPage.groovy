package se.inera.certificate.web.pages


class ConsentPage extends AbstractLoggedInPage {

    static at = { doneLoading() && consentTerms.isDisplayed() }

    static content = {
        consentTerms { $("#consentTerms") }
        giveConsentButton { $("#giveConsentButton") }
        consentGivenView(required: false) { $("#consent-given") }
    }

    def giveConsent() {
        giveConsentButton.click()
    }
}
