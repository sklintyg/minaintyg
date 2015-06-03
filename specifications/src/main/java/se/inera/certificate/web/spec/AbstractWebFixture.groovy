package se.inera.certificate.web.spec

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
        }
    }

    public boolean inkorgsidanVisas() {
        Browser.drive {
            waitFor {
                at InboxPage
            }
        }
    }

    public boolean arkiveradesidanVisas() {
        Browser.drive {
            waitFor {
                at ArchivedPage
            }
        }
    }

    public boolean geSamtyckesidanVisas() {
        Browser.drive {
            waitFor {
                at ConsentPage
            }
        }
    }

    public boolean omMinaIntygsidanVisas() {
        Browser.drive {
            waitFor {
                at AboutMinaIntygPage
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

    public void laddaOm() {
        Browser.drive {
            getJs().exec([], "window.location.reload()")
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
