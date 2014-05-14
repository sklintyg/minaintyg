package se.inera.certificate.web.spec

import se.inera.certificate.web.pages.SendCertificateSentPage
import se.inera.certificate.web.pages.SendCertificateChooseRecipientPage
import se.inera.certificate.web.pages.SendCertificateSummaryPage

public class SkickaIntyg {


    public boolean väljMottagareVynVisas() {
        Browser.drive {
            waitFor {
                at SendCertificateChooseRecipientPage
            }
            return page.confirmRecipientSelectionBtn.isDisplayed()
        }
    }

    public void väljMottagare(String mottagare) {
        Browser.drive {
            page.chooseRecipient(mottagare)
        }
    }

    public void bekräftaValdMottagare() {
        Browser.drive {
            page.confirmRecipientSelection()
        }
    }

    public boolean summeringsVynVisas() {
        Browser.drive {
            waitFor {
                at SendCertificateSummaryPage
            }
            return page.confirmAndSendBtn.isDisplayed()
        }
    }
    
    public boolean valdMottagareÄr(String mottagare) {
        Browser.drive {
            waitFor {
                at SendCertificateSummaryPage
            }
            return page.selectedRecipientMessage.text() == mottagare
        }
    }

    public void bekräftaSkickaIntyg() {
        Browser.drive {
            page.confirmSend()
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
            return page.alreadySentWarningMessage.isDisplayed()
        }
    }
    
    public boolean redanSkickatVarningVisasInte() {
        Browser.drive {
            return !page.alreadySentWarningMessage.isDisplayed()
        }
    }

    public void gåTillbakaTillIntyget() {
        Browser.drive {
            page.backToCertificateView()
        }
    }

}
