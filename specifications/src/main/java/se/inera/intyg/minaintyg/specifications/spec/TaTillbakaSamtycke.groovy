package se.inera.intyg.minaintyg.specifications.spec;

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.minaintyg.specifications.pages.AboutMinaIntygPage
import se.inera.intyg.minaintyg.specifications.pages.InboxPage

public class TaTillbakaSamtycke extends AbstractWebFixture {

    public void gåTillOmMinaIntygsidan() {
        Browser.drive {
            page.goToAboutPage()
        }
    }

    public void gåTillOmSamtyckesidan() {
        Browser.drive {
            page.gotoAboutConsentSection()
        }
    }

    public boolean omSamtyckeAvsnittVisas() {
        boolean result
        Browser.drive {
            result = page.aboutSamtyckeSection.isDisplayed()
        }
        return result
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
