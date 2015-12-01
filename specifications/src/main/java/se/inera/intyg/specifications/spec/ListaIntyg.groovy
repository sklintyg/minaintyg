package se.inera.intyg.specifications.spec

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.specifications.pages.ArchivedPage
import se.inera.intyg.specifications.pages.InboxPage

public class ListaIntyg extends AbstractWebFixture {

    public boolean listaMedIntygVisas() {
        boolean result
        Browser.drive {
            result = page.certificateTable.isDisplayed()
        }
        return result
    }

    public boolean listaMedArkiveradeIntygVisas() {
        boolean result
        Browser.drive {
            result = page.certificateTable.isDisplayed()
        }
        return result
    }

    public boolean finnsIngaIntyg() {
        boolean result
        Browser.drive {
            result = page.noCertificates.isDisplayed()
        }
        return result
    }

    public void arkiveraIntyg(String id) {
        Browser.drive {
            page.archiveCertificate(id)
        }
    }

    public void konfirmeraArkiveraIntyg() {
        Browser.drive {
            page.confirmArchiveCertificate()
        }
    }

    public void återställIntyg(String id) {
        Browser.drive {
            page.restoreCertificate(id)
        }
    }

    public void konfirmeraÅterställIntyg() {
        Browser.drive {
            page.confirmRestoreCertificate()
        }
    }

    public void gåTillArkiveradeIntyg() {
        Browser.drive {
            page.goToArchivedPage()
        }
    }

    public void gåTillInkorgen() {
        Browser.drive {
            page.goToInboxPage()
        }
    }

    public boolean arkiveratIntygFinnsIListan(String id) {
        boolean result
        Browser.drive {
            result = page.certificateExists(id)
        }
        return result
    }

    public boolean intygFinnsIListan(String id) {
        boolean result
        Browser.drive {
            result = page.certificateExists(id)
        }
        return result
    }

    public boolean rättatIntygVisasKorrekt(String id) {
        boolean result
        Browser.drive {
            result = page.cancelledCertificateDisplayed(id);
        }
        return result
    }

    public void visaIntyg(String id) {
        Browser.drive {
            page.viewCertificate(id)
        }
    }

    public boolean complementaryInfoArSatt(String id) {
        boolean result
        Browser.drive {
            result = !page.complementaryInfoIsSet(id).isEmpty()
        }
        return result
    }
}
