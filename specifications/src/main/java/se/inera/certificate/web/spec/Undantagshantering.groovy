package se.inera.certificate.web.spec;

import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.NotFoundPage

public class Undantagshantering {

    public void gåTillSidaSomInteFinns() {
        Browser.drive {
            go "../okändsida"
            waitFor {
                at NotFoundPage
            }
        }
    }

    public boolean sidanFinnsEjVisas() {
        boolean result
        Browser.drive {
            result = at NotFoundPage
        }
        return result
    }
}
