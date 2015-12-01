package se.inera.intyg.specifications.pages

import se.inera.intyg.common.specifications.page.AbstractPage

class MvkLoginPage extends AbstractPage {

    static at = { $("div", class: "information").isDisplayed() }
}
