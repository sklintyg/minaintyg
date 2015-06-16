package se.inera.certificate.web.spec

import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.ArchivedPage

class Navigering {

    public def gåTillArkiveradeIntyg() {
        Browser.drive {
            page.goToArchivedPage()
        }
    }

    public boolean sidTitelÄr(titel) {
        Browser.getTitle() == titel
    }

    public boolean valdMenyflikÄr(id) {
        def result
        Browser.drive {
            result = (page.activeTab() == id)
        }
        result
    }
}
