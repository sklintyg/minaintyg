package se.inera.intyg.minaintyg.specifications.pages


class ConsentGivenPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#consent-given").isDisplayed() }

    static content = {
        continueToMIButton(to: InboxPage, toWait: true) { $("#continueToMI") }
    }

    def continueToMI() {
        continueToMIButton.click()
    }
}
