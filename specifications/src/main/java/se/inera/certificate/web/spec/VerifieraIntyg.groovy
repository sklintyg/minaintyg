package se.inera.certificate.web.spec

import se.inera.certificate.spec.Browser

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
