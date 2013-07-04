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
            at ConsentPage
            page.goBackToMvk()
        }
    }

    public void loggaUt() {
        Browser.drive {
            at ConsentPage
            page.logout()
        }
    }

    public boolean mvksInloggningssidaVisas() {
        Browser.drive {
            waitFor {
                at MvkLoginPage
            }
        }
    }

    public boolean mvksUtloggningssidaVisas() {
        Browser.drive {
            waitFor {
                at MvkLogoutPage
            }
        }
    }

    public void gåTillStartsida() {
        Browser.drive {
            go "/web/start"
        }
    }

    public boolean accessDeniedVisas() {
        Browser.drive {
            waitFor {
                at AccessDeniedPage
            }
        }
    }
}
