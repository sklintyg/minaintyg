package se.inera.certificate.web.spec

import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.ArchivedPage
import se.inera.certificate.web.pages.InboxPage

public class ListaIntyg {

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

    public boolean listaMedIntygVisas() {
        Browser.drive {
            waitFor {
                at InboxPage
            }
            return page.certificateTable.isDisplayed()
        }
    }

    public boolean listaMedArkiveradeIntygVisas() {
        Browser.drive {
            waitFor {
                at ArchivedPage
            }
            return page.certificateTable.isDisplayed()
        }
    }

    public boolean finnsIngaIntyg() {
        Browser.drive {
            waitFor {
                at InboxPage
            }
            return page.noCertificates.isDisplayed()
        }
    }

    public void arkiveraIntyg(String id) {
        Browser.drive {
            page.archiveCertificate(id)
        }
    }

    public void konfirmeraArkiveraIntyg() {
        Browser.drive {
            waitFor (message: "no button") {
                confirmArchiveButton.displayed
            }
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
            waitFor {
                confirmRestoreButton.displayed
            }
            page.confirmRestoreCertificate()
        }
    }

    public void gåTillArkiveradeIntyg() {
        Browser.drive {
            page.goToArchivedTab()
        }
    }

    public void gåTillInkorgen() {
        Browser.drive {
            page.goToInboxPage()
        }
    }

    public boolean arkiveratIntygFinnsIListan(String id) {
        Browser.drive {
            waitFor {
                at ArchivedPage
            }
            return page.certificateExists(id)
        }
    }

    public boolean arkiveratIntygFinnsEjIListan(String id) {
        Browser.drive {
            waitFor {
                at ArchivedPage
                !page.certificateExists(id)
            }
            return !page.certificateExists(id)
        }
    }

    public boolean intygFinnsIListan(String id) {
        Browser.drive {
            waitFor {
                at InboxPage
                page.certificateExists(id)
            }
            return page.certificateExists(id)
        }
    }

    public boolean intygFinnsEjIListan(String id) {
        Browser.drive {
            waitFor {
                at InboxPage
                !page.certificateExists(id)
            }
            return !page.certificateExists(id)
        }
    }

    public boolean rättatIntygVisasKorrekt(String id) {
        Browser.drive {
            waitFor {
                at InboxPage
            }
            return page.cancelledCertificateDisplayed(id);
        }
    }

    public void visaIntyg(String id) {
        Browser.drive {
            page.viewCertificate(id)
        }
    }

    public boolean complementaryInfoArSatt(String id) {
        Browser.drive {
            waitFor {
                at InboxPage
            }
            return !page.complementaryInfoIsSet(id).isEmpty()
        }
    }
}
