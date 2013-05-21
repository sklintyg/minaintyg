package se.inera.certificate.web.pages

import geb.Page

class NotFoundPage extends Page {

    static at = { $("#notFound").isDisplayed() }

}
