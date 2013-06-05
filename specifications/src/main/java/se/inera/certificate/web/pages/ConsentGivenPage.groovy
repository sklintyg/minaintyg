package se.inera.certificate.web.pages

import geb.Page

class ConsentGivenPage extends Page {

    static at = { $("#consent-given").isDisplayed() }

    static content = {
        continueToMIButton(required: false) { $("#continueToMI") }
    }

    def continueToMI() {
        continueToMIButton.click()
    }
}
