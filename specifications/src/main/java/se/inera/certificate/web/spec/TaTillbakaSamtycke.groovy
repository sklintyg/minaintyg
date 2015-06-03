package se.inera.certificate.web.spec;

import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.AboutMinaIntygPage
import se.inera.certificate.web.pages.ConsentPage

public class TaTillbakaSamtycke extends AbstractWebFixture {

    public void gåTillOmMinaIntygsidan() {
        Browser.drive {
            page.goToAboutMinaIntyg()
        }
    }

    public void gåTillOmSamtyckesidan() {
        Browser.drive {
            page.gotoAboutConsentSection()
        }
    }

    public boolean omSamtyckeAvsnittVisas() {
        Browser.drive {
            waitFor {
                at AboutMinaIntygPage
            }
            return page.aboutSamtyckeSection.isDisplayed()
        }
    }

    public void väljÅtertaSamtycke() {
        Browser.drive {
            page.openRevokeConsentDialog()
        }
    }

    public void bekräftaÅtertaSamtycke() {
        Browser.drive {
            page.revokeConsent()
        }
    }

}
