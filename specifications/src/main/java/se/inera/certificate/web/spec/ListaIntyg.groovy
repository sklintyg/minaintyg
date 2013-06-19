package se.inera.certificate.web.spec

import geb.Browser
import se.inera.certificate.web.pages.InboxPage
import se.inera.certificate.web.pages.ArchivedPage

public class ListaIntyg {

    public void loggaPåSom(String pnr) {
        Browser.drive {
            go "sso?guid=${pnr}"
        }
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

    public boolean inkorgsidanVisas() {
        verifyAtPage(InboxPage)
    }

    public boolean arkiveradesidanVisas() {
        verifyAtPage(ArchivedPage)
    }

    public boolean listaMedIntygVisas() {
        def result = false
        Browser.drive {
            at(InboxPage)
            result = page.certificateTable.isDisplayed()
        }
        result
    }

    public boolean listaMedArkiveradeIntygVisas() {
        def result = false
        Browser.drive {
            at(ArchivedPage)
            result = page.certificateTable.isDisplayed()
        }
        result
    }

    public boolean finnsIngaIntyg() {
        def result = false
        Browser.drive {
            at(InboxPage)
            result = page.noCertificates.isDisplayed()
        }
        result
    }

    public void arkiveraIntyg(String id) {
        Browser.drive {
            at(InboxPage)
            page.archiveCertificate(id)
        }
    }

    public void återställIntyg(String id) {
        Browser.drive {
            at(ArchivedPage)
            page.restoreCertificate(id)
        }
    }

    public void gåTillArkiveradeIntyg() {
        Browser.drive {
            at(InboxPage)
            page.goToArchivedTab()
        }
    }

    public void gåTillInkorgen() {
        Browser.drive {
            at(ArchivedPage)
            page.goToInboxPage()
        }
    }

    public boolean arkiveratIntygFinnsIListan(String id) {
        def result = false
        Browser.drive {
            at(ArchivedPage)
            result = page.certificateExists(id)
        }
        result
    }

    public boolean arkiveratIntygFinnsEjIListan(String id) {
        def result = false
        Browser.drive {
            at(ArchivedPage)
            result = !page.certificateExists(id)
        }
        result
    }

    public boolean intygFinnsIListan(String id) {
        def result = false
        Browser.drive {
            at(InboxPage)
            result = page.certificateExists(id)
        }
        result
    }

    public boolean intygFinnsEjIListan(String id) {
        def result = false
        Browser.drive {
            at(InboxPage)
            result = !page.certificateExists(id)
        }
        result
    }

    public boolean rättatIntygVisasKorrekt(String id) {
        def result = false
        Browser.drive {
            at(InboxPage)
            result = page.cancelledCertificateDisplayed(id);
        }
        result
    }

    public void visaIntyg(String id) {
        Browser.drive {
            at(InboxPage)
            page.viewCertificate(id)
        }
    }

    public void waitFor(long millis) {
        System.sleep(millis)
    }
}
