package se.inera.certificate.web.spec;

import geb.Browser

import se.inera.certificate.web.pages.InboxPage
import se.inera.certificate.web.pages.NotFoundPage

public class Undantagshantering {

    public void gåTillSidaSomInteFinns() {
        Browser.drive {
            go "../okändsida"
        }
    }

    public boolean sidanFinnsEjVisas() {
        Browser.drive {
            at(NotFoundPage)
        }
    }
}
