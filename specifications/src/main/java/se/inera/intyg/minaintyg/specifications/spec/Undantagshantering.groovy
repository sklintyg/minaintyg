package se.inera.intyg.minaintyg.specifications.spec;

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.minaintyg.specifications.pages.NotFoundPage

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
