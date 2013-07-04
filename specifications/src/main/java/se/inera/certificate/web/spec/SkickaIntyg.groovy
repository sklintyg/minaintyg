package se.inera.certificate.web.spec

import geb.Browser
import se.inera.certificate.web.pages.SendCertificateFlowPage

public class SkickaIntyg {


    public boolean summeringsVynVisas() {
        def result = false
        Browser.drive {
            waitFor {
                at SendCertificateFlowPage
            }
            result = page.confirmAndSendBtn.isDisplayed()
        }
        result
    }

    public void bekräftaSkickaIntyg() {
        Browser.drive {
            at SendCertificateFlowPage
            page.confirmSendFlow()
        }
    }

    public boolean resultatSidanVisas() {
        def result = false
        Browser.drive {
            waitFor {
                at SendCertificateFlowPage
            }
            result = page.resultMessageContainer.isDisplayed()
        }
        result
    }

    public boolean redanSkickatVarningVisas() {
        def result = false
        Browser.drive {
            waitFor {
                at SendCertificateFlowPage
            }
            result = page.alreadySentWarningMessage.isDisplayed()
        }
        result
    }

    public void gåTillbakaTillIntyget() {
        Browser.drive {
            at SendCertificateFlowPage
            page.backToCertificateView()
        }
    }

}
