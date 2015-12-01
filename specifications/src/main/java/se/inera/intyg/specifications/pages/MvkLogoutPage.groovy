package se.inera.certificate.web.pages

import se.inera.intyg.common.specifications.page.AbstractPage

class MvkLogoutPage extends AbstractPage {

    static at = { $("div", class: "logoutWrap").isDisplayed() }

}
