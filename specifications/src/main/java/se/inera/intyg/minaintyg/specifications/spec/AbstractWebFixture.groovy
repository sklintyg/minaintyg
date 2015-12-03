package se.inera.intyg.minaintyg.specifications.spec

import se.inera.intyg.common.specifications.page.AbstractPage
import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.minaintyg.specifications.pages.AboutMinaIntygPage
import se.inera.intyg.minaintyg.specifications.pages.AccessDeniedPage
import se.inera.intyg.minaintyg.specifications.pages.ArchivedPage
import se.inera.intyg.minaintyg.specifications.pages.ConsentPage
import se.inera.intyg.minaintyg.specifications.pages.InboxPage

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
