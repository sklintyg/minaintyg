package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class AccessDeniedPage extends AbstractPage {
    static at = { doneLoading() && $("#noAuth").isDisplayed() }
}
