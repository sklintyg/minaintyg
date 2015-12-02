package se.inera.intyg.minaintyg.specifications.spec

import se.inera.intyg.common.specifications.spec.Browser

class VerifieraIntyg {

    public VerifieraIntyg() {
    }

    def methodMissing(String name, args) {
        getStringResult(name)
    }

    String getStringResult(field) {
        def result = ''
        Browser.drive {
            if (!page."$field".isDisplayed()) {
                result = "notshown"
            } else {
                result = page."$field".text().trim()
            }
        }
        result
    }

}
