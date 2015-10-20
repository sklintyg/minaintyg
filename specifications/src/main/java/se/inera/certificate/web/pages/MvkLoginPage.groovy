package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class MvkLoginPage extends AbstractPage {

    static at = { $("div", class: "information").isDisplayed() }
}
