package se.inera.certificate.web.spec

import geb.Browser
import se.inera.certificate.web.pages.AccessDeniedPage
import se.inera.certificate.web.pages.ArchivedPage
import se.inera.certificate.web.pages.ConsentPage
import se.inera.certificate.web.pages.InboxPage
import se.inera.certificate.web.pages.MvkLoginPage
import se.inera.certificate.web.pages.MvkLogoutPage

public class LoggaUt {

    public void tillbakaTillMvk() {
        Browser.drive {
            assert at(ConsentPage)
            page.goBackToMvk()
        }
    }

    public void loggaUt() {
        Browser.drive {
            assert at(ConsentPage)
            page.logout()
        }
    }

    public boolean mvksInloggningssidaVisas() {
        verifyAtPage(MvkLoginPage)
    }

    public boolean mvksUtloggningssidaVisas() {
        verifyAtPage(MvkLogoutPage)
    }

    public void gåTillStartsida() {
        Browser.drive {
            go "/web/start"
        }
    }

    public boolean accessDeniedVisas() {
        verifyAtPage(AccessDeniedPage)
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
