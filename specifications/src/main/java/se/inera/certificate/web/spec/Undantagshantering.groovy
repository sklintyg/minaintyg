package se.inera.certificate.web.spec;

import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.NotFoundPage

public class Undantagshantering {

    public void gåTillSidaSomInteFinns() {
        Browser.drive {
            go "../okändsida"
        }
    }

    public boolean sidanFinnsEjVisas() {
        Browser.drive {
            waitFor {
                at NotFoundPage
            }
        }
    }
}
