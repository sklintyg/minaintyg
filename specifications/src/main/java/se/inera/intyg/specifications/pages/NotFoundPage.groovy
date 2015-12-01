package se.inera.intyg.specifications.pages

import se.inera.intyg.common.specifications.page.AbstractPage

class NotFoundPage extends AbstractPage {

    static at = { $("#notFound").isDisplayed() }
}
