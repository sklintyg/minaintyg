package se.inera.certificate.web.spec

import se.inera.certificate.web.pages.SendCertificateSentPage
import se.inera.certificate.web.pages.SendCertificateSummaryPage

public class SkickaIntyg {


    public boolean summeringsVynVisas() {
        Browser.drive {
            waitFor {
                at SendCertificateSummaryPage
            }
            return page.confirmAndSendBtn.isDisplayed()
        }
    }

    public void bekräftaSkickaIntyg() {
        Browser.drive {
            page.confirmSendFlow()
        }
    }

    public boolean resultatSidanVisas() {
        Browser.drive {
            waitFor {
                at SendCertificateSentPage
            }
            return page.resultMessageContainer.isDisplayed()
        }
    }

    public boolean redanSkickatVarningVisas() {
        Browser.drive {
            waitFor {
                at SendCertificateSummaryPage
            }
            return page.alreadySentWarningMessage.isDisplayed()
        }
    }

    public void gåTillbakaTillIntyget() {
        Browser.drive {
            page.backToCertificateView()
        }
    }

}
