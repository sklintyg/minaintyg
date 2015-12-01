package se.inera.intyg.specifications.pages


class ConsentDeniedPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#refuseConsentMessage").isDisplayed() }
}
