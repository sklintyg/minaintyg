package se.inera.intyg.minaintyg.specifications.pages


class ConsentDeniedPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#refuseConsentMessage").isDisplayed() }
}
