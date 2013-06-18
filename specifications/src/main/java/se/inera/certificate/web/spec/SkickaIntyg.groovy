package se.inera.certificate.web.spec

import geb.Browser
import se.inera.certificate.web.pages.SendCertificateFlowPage

public class SkickaIntyg {



    public boolean summeringsVynVisas() {
        def result = false
        Browser.drive {
            at(SendCertificateFlowPage)
            result = page.confirmAndSendBtn.isDisplayed()
        }
        result
    }

    public void bekräftaSkickaIntyg() {
        Browser.drive {
            assert at(SendCertificateFlowPage)
            page.confirmSendFlow()
        }
    }
    
    public boolean resultatSidanVisas() {
        def result = false
        Browser.drive {
            at(SendCertificateFlowPage)
            result = page.resultMessageContainer.isDisplayed()
        }
        result
    }
    public void gåTillbakaTillIntyget() {
        Browser.drive {
            assert at(SendCertificateFlowPage)
            page.backToCertificateView()
        }
    }
    public void waitFor(long millis) {
        System.sleep(millis)
    }
}
