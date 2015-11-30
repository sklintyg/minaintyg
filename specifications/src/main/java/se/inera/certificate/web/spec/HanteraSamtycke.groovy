package se.inera.certificate.web.spec;

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.certificate.web.pages.ConsentDeniedPage
import se.inera.certificate.web.pages.ConsentGivenPage
import se.inera.certificate.web.pages.ConsentPage
import se.inera.certificate.web.pages.InboxPage

public class HanteraSamtycke extends AbstractWebFixture {

    public void geSamtycke() {
        Browser.drive {
            page.giveConsent()
        }
    }

    public void nekaSamtycke() {
        Browser.drive {
            page.denyConsent()
        }
    }

    public boolean samtyckeGivetSidanVisas() {
        boolean result
        Browser.drive {
            result = at ConsentGivenPage
        }
        return result
    }

    public void gåVidareTillMinaIntyg() {
        Browser.drive {
            page.continueToMI()
        }
    }

    public boolean samtyckeNekatSidanVisas() {
        boolean result
        Browser.drive {
            result = at ConsentDeniedPage
        }
        return result
    }

}
