package se.inera.intyg.specifications.spec

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.specifications.pages.ArchivedPage

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
