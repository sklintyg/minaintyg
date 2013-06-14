package se.inera.certificate.web.pages

import geb.Page

class MvkLogoutPage extends Page {

    static at = { $("div", class: "logoutWrap").isDisplayed() }

}
