package se.inera.certificate.web.pages


class ConsentGivenPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#consent-given").isDisplayed() }

    static content = {
        continueToMIButton(required: false) { $("#continueToMI") }
    }

    def continueToMI() {
        continueToMIButton.click()
    }
}
