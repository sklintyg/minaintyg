package se.inera.certificate.web.pages

import geb.Page

class AccessDeniedPage extends Page {
    static at = { $("#noAuth").isDisplayed() }
}
