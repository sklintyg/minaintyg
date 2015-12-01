package se.inera.intyg.specifications.spec

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.specifications.pages.AccessDeniedPage
import se.inera.intyg.specifications.pages.MvkLoginPage
import se.inera.intyg.specifications.pages.MvkLogoutPage

public class LoggaUt extends AbstractWebFixture {

    public void tillbakaTillMvk() {
        Browser.drive {
            page.goBackToMvk()
        }
    }

    public void loggaUt() {
        Browser.drive {
            page.logout()
        }
    }

    public boolean mvksInloggningssidaVisas() {
        boolean result
        Browser.drive {
            result = at MvkLoginPage
        }
        return result
    }

    public boolean mvksUtloggningssidaVisas() {
        boolean result
        Browser.drive {
            result = at MvkLogoutPage
        }
        return result
    }
}
