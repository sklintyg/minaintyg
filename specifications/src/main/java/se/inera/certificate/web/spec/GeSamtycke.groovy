package se.inera.certificate.web.spec;

import geb.Browser
import se.inera.certificate.web.pages.ArchivedPage
import se.inera.certificate.web.pages.InboxPage
import se.inera.certificate.web.pages.ConsentPage
import se.inera.certificate.web.pages.ConsentGivenPage
import se.inera.certificate.web.pages.ConsentDeniedPage

public class GeSamtycke {

    private boolean verifyAtPage(def page) {
        def result = false
        try {
            Browser.drive {
                assert at(page)
            }
            result = true
        } catch (AssertionError e) {
            // Do nothing - should be false
        }
        result
    }

    public void loggaPåSom(String pnr) {
        Browser.drive {
            go "sso?guid=${pnr}"
        }
    }

    public void geSamtycke() {
        Browser.drive {
            assert at(ConsentPage)
            page.giveConsent()

        }
    }

    public void nekaSamtycke() {
        Browser.drive {
            assert at(ConsentPage)
            page.denyConsent()
        }
    }

    public boolean inkorgSidanVisas() {
        verifyAtPage(InboxPage)
    }

    public boolean geSamtyckeSidanVisas() {
        verifyAtPage(ConsentPage)
    }

    public boolean samtyckeGivetSidanVisas() {
        verifyAtPage(ConsentGivenPage)
    }

    public void gåVidareTillMinaIntyg() {
        Browser.drive {
            at(ConsentGivenPage)
            page.continueToMI()
        }
    }

    public boolean samtyckeNekatSidanVisas() {
        verifyAtPage(ConsentDeniedPage)
    }

}
