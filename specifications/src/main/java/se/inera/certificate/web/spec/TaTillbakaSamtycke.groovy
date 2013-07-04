package se.inera.certificate.web.spec;

import geb.Browser

import se.inera.certificate.web.pages.InboxPage
import se.inera.certificate.web.pages.AboutMinaIntygPage
import se.inera.certificate.web.pages.ConsentPage

public class TaTillbakaSamtycke {

    public void gåTillOmMinaIntygSidan() {
        Browser.drive {
            at InboxPage
            page.goToAboutMinaIntyg()
        }
    }

    public boolean omMinaIntygSidanVisas() {
        Browser.drive {
            waitFor {
                at AboutMinaIntygPage
            }
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
            waitFor {
                at AboutMinaIntygPage
            }
            assert page.aboutSamtyckeSection.isDisplayed()
        }
    }

    public boolean väljÅtertaSamtycke() {
        Browser.drive {
            waitFor {
                at AboutMinaIntygPage
            }
            page.openRevokeConsentDialog()
        }
    }

    public void bekräftaÅtertaSamtycke() {
        Browser.drive {
            waitFor {
                at AboutMinaIntygPage
            }
            page.revokeConsent()
        }
    }

    public boolean samtyckeSidanVisas() {
        Browser.drive {
            waitFor {
                at ConsentPage
            }
        }
    }

}
