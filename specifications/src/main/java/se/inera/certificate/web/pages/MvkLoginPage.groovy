package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class MvkLoginPage extends AbstractPage {

    static at = { $("h2", class: "loginSubTitle").isDisplayed() }
}
