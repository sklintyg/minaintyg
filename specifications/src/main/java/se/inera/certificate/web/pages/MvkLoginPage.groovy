package se.inera.certificate.web.pages

import geb.Page

class MvkLoginPage extends Page {

    static at = { $("h2", class: "loginSubTitle").isDisplayed() }

}
