package se.inera.certificate.web.pages


class ConsentDeniedPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#refuseConsentMessage").isDisplayed() }
}
