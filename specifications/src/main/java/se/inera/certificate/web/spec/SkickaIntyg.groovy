package se.inera.certificate.web.spec

import geb.Browser
import se.inera.certificate.web.pages.SendCertificateSentPage
import se.inera.certificate.web.pages.SendCertificateSummaryPage

public class SkickaIntyg {


    public boolean summeringsVynVisas() {
        def result = false
        Browser.drive {
            waitFor {
                at SendCertificateSummaryPage
            }
            result = page.confirmAndSendBtn.isDisplayed()
        }
        result
    }

    public void bekräftaSkickaIntyg() {
        Browser.drive {
            at SendCertificateSummaryPage
            page.confirmSendFlow()
        }
    }

    public boolean resultatSidanVisas() {
        def result = false
        Browser.drive {
            waitFor {
                at SendCertificateSentPage
            }
            result = page.resultMessageContainer.isDisplayed()
        }
        result
    }

    public boolean redanSkickatVarningVisas() {
        def result = false
        Browser.drive {
            waitFor {
                at SendCertificateSummaryPage
            }
            result = page.alreadySentWarningMessage.isDisplayed()
        }
        result
    }

    public void gåTillbakaTillIntyget() {
        Browser.drive {
            at SendCertificateSentPage
            page.backToCertificateView()
        }
    }

}
