package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class MvkLogoutPage extends AbstractPage {

    static at = { $("div", class: "logoutWrap").isDisplayed() }

}
