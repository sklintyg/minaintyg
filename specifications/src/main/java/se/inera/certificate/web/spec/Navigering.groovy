package se.inera.certificate.web.spec

import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.ArchivedPage

class Navigering {

    public def gåTillArkiveradeIntyg() {
        Browser.drive {
            page.$("#archivedTab").click()
            waitFor {
                at ArchivedPage
            }
        }
    }

    public boolean sidTitelÄr(titel) {
        Browser.getTitle() == titel
    }

    public boolean valdMenyflikÄr(id) {
        def result
        Browser.drive {
            result = (page.$('.navbar-nav li.active > a').getAt(0).getAttribute('id') == id)
        }
        result
    }
}
