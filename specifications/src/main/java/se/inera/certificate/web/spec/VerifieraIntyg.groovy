package se.inera.certificate.web.spec

import org.codehaus.groovy.runtime.StackTraceUtils
import se.inera.certificate.spec.Browser

class VerifieraIntyg {

    public VerifieraIntyg() {

    }

    String getCurrentMethodName(){
        def marker = new Throwable()
        return StackTraceUtils.sanitize(marker).stackTrace[1].methodName
    }

    boolean getBooleanResult(field) {
        def result = false
        Browser.drive {
            result = page."$field".isDisplayed()
        }
        result
    }

    String getStringResult(field) {
        def result = ''
        Browser.drive {
            if (!page."$field".isDisplayed()) {
                result = "notshown"
            } else {
                result = page."$field".text()
            }
        }
        result
    }

}
