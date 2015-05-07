package se.inera.certificate.web.spec
import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.IntygFk7263Page
import se.inera.certificate.web.pages.IntygTsBasPage
import se.inera.certificate.web.pages.IntygTsDiabetesPage

public class VisaIntyg {

    public boolean intygssidanVisas(typ="") {
        Browser.drive {
            waitFor {
                if (typ == "ts-bas")
                    at IntygTsBasPage
                else if (typ == "ts-diabetes")
                    at IntygTsDiabetesPage
                else
                    at IntygFk7263Page
            }
        }
    }

    public boolean intygetsTypÄr(String typ) {
        boolean result
        Browser.drive {
           result = $("#certType", title: typ).size() == 1
        }
        result
    }

    public boolean intygetsIdÄr(String id) {
        boolean result
        Browser.drive {
            result = $("#certId", title: id).size() == 1
        }
        result
    }

    public void väljSkickaIntyg() {
        Browser.drive {
            page.startSendFlow()
        }
    }

    public void väljArkiveraIntyg() {
        Browser.drive {
            page.archive();
        }
    }

    public void konfirmeraArkiveraIntyg() {
        Browser.drive {
            waitFor {
                confirmArchiveBtn.displayed
            }
            page.confirmArchiveCertificate()
        }
    }

}
