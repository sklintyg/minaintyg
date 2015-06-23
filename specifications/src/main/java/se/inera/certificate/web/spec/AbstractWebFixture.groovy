package se.inera.certificate.web.spec

import se.inera.certificate.page.AbstractPage
import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.AboutMinaIntygPage
import se.inera.certificate.web.pages.AccessDeniedPage
import se.inera.certificate.web.pages.ArchivedPage
import se.inera.certificate.web.pages.ConsentPage
import se.inera.certificate.web.pages.InboxPage

public class AbstractWebFixture {

    public void loggaPåSom(String pnr) {
        Browser.drive {
            go "sso?guid=${pnr}"
            waitFor {
                AbstractPage.doneLoading()
            }
        }
    }

    public boolean inkorgsidanVisas() {
        boolean result
        Browser.drive {
            result = at InboxPage
        }
        return result
    }

    public boolean arkiveradesidanVisas() {
        boolean result
        Browser.drive {
            result = at ArchivedPage
        }
        return result
    }

    public boolean geSamtyckesidanVisas() {
        boolean result
        Browser.drive {
            result = at ConsentPage
        }
    }

    public boolean omMinaIntygsidanVisas() {
        boolean result
        Browser.drive {
            result = at AboutMinaIntygPage
        }
        return result
    }

    public void gåTillStartsida() {
        Browser.drive {
            go "/web/start"
            waitFor {
                AbstractPage.doneLoading()
            }
        }
    }

    public boolean accessDeniedVisas() {
        boolean result
        Browser.drive {
            result = at AccessDeniedPage
        }
        return result
    }

    public void laddaOm() {
        Browser.drive {
            getJs().exec([], "window.location.reload()")
            waitFor {
                AbstractPage.doneLoading()
            }
        }
    }
    
    public void besökExternSida() {
        Browser.drive {
            go "http://www.google.com"
            waitFor {
                title == "Google"
            }
        }
    }

    public void väntaSekunder(int sekunder) {
        Thread.sleep(sekunder * 1000)
    }
}
