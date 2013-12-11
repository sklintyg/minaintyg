package se.inera.certificate.web.spec;

import se.inera.certificate.web.pages.ConsentDeniedPage
import se.inera.certificate.web.pages.ConsentGivenPage
import se.inera.certificate.web.pages.ConsentPage
import se.inera.certificate.web.pages.InboxPage

public class GeSamtycke {

    public void loggaPåSom(String pnr) {
        Browser.drive {
            go "sso?guid=${pnr}"
        }
    }

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

    public boolean inkorgSidanVisas() {
        Browser.drive {
            waitFor {
                at InboxPage
            }
        }
    }

    public boolean geSamtyckeSidanVisas() {
        Browser.drive {
            waitFor {
                at ConsentPage
            }
        }
    }

    public boolean samtyckeGivetSidanVisas() {
        Browser.drive {
            waitFor {
                at ConsentGivenPage
            }
        }
    }

    public void gåVidareTillMinaIntyg() {
        Browser.drive {
            page.continueToMI()
        }
    }

    public boolean samtyckeNekatSidanVisas() {
        Browser.drive {
            at ConsentDeniedPage
        }
    }

}
