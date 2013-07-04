package se.inera.certificate.web.spec;

import geb.Browser

import se.inera.certificate.web.pages.InboxPage
import se.inera.certificate.web.pages.AboutMinaIntygPage
import se.inera.certificate.web.pages.ConsentPage

public class TaTillbakaSamtycke {

    public void loggaPåSom(String pnr) {
        Browser.drive { go "sso?guid=${pnr}" }
    }

    public boolean inkorgSidanVisas() {
        Browser.drive {
            at InboxPage
        }
    }

    public void gåTillOmMinaIntygSidan() {
        Browser.drive {
            at InboxPage
            page.goToAboutMinaIntyg()
        }
    }

    public boolean omMinaIntygSidanVisas() {
        Browser.drive {
            at AboutMinaIntygPage
        }
    }

    public void gåTillOmSamtyckeSidan() {
        Browser.drive {
            at AboutMinaIntygPage
            page.gotoAboutConsentSection()
        }
    }

    public void omSamtyckeAvsnittVisas() {
        Browser.drive {
            at AboutMinaIntygPage
            assert page.aboutSamtyckeSection.isDisplayed()
        }
    }

    public boolean väljÅtertaSamtycke() {
        Browser.drive {
            at AboutMinaIntygPage
            page.openRevokeConsentDialog()
        }
    }

    public void bekräftaÅtertaSamtycke() {
        Browser.drive {
            at AboutMinaIntygPage
            page.revokeConsent()
        }
    }

    public boolean samtyckeSidanVisas() {
        Browser.drive {
            at ConsentPage
        }
    }

    public void waitFor(long millis) {
        System.sleep(millis)
    }

}
