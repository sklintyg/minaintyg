package se.inera.certificate.web.spec

import se.inera.certificate.spec.Browser
import se.inera.certificate.web.pages.SendCertificateChooseRecipientPage
import se.inera.certificate.web.pages.SendCertificateSentPage
import se.inera.certificate.web.pages.SendCertificateSummaryPage

public class SkickaIntyg {

    public boolean väljMottagareVynVisas() {
        boolean result
        Browser.drive {
            result = page.confirmRecipientSelectionBtn.isDisplayed()
        }
        return result
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
        boolean result
        Browser.drive {
            result = page.confirmAndSendBtn.isDisplayed()
        }
        return result
    }
    
    public boolean valdMottagareÄr(String mottagare) {
        boolean result
        Browser.drive {
            result = page.selectedRecipientMessage.text() == mottagare
        }
        return result
    }

    public void bekräftaSkickaIntyg() {
        Browser.drive {
            page.confirmSend()
        }
    }

    public boolean resultatSidanVisas() {
        boolean result
        Browser.drive {
            result = page.resultMessageContainer?.isDisplayed()
        }
        return result
    }

    public boolean redanSkickatVarningVisas() {
        boolean result
        Browser.drive {
            result = page.alreadySentWarningMessage?.isDisplayed()
        }
        return result
    }
    
    public void gåTillbakaTillIntyget() {
        Browser.drive {
            page.backToCertificateView()
        }
    }

}
