package se.inera.certificate.web.pages

import se.inera.intyg.common.specifications.page.AbstractPage

class AccessDeniedPage extends AbstractPage {
    static at = { doneLoading() && $("#noAuth").isDisplayed() }
}
