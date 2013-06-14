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
        verifyAtPage(InboxPage)
    }

    public void gåTillOmMinaIntygSidan() {
        Browser.drive {
            assert at(InboxPage)
            page.goToAboutMinaIntyg()
        }
    }

    public boolean omMinaIntygSidanVisas() {
        verifyAtPage(AboutMinaIntygPage)
    }

    public void gåTillOmSamtyckeSidan() {
        Browser.drive {
            assert at(AboutMinaIntygPage)
            page.gotoAboutConsentSection()
        }
    }

    public void omSamtyckeAvsnittVisas() {
        Browser.drive {
            assert at(AboutMinaIntygPage)
            assert page.aboutSamtyckeSection.isDisplayed()
        }
    }

    public boolean väljÅtertaSamtycke() {
        Browser.drive {
            at(AboutMinaIntygPage)
            page.openRevokeConsentDialog()
        }
    }

    public void bekräftaÅtertaSamtycke() {
        Browser.drive {
            assert at(AboutMinaIntygPage)
            page.revokeConsent()
        }
    }

    public boolean samtyckeSidanVisas() {
        verifyAtPage(ConsentPage)
    }

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

    public void waitFor(long millis) {
        System.sleep(millis)
    }

}
