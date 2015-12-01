package se.inera.certificate.web.pages

import se.inera.intyg.common.specifications.page.AbstractPage

class NotFoundPage extends AbstractPage {

    static at = { $("#notFound").isDisplayed() }
}
