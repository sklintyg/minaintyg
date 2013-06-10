package se.inera.certificate.web.pages

import geb.Page

class AboutMinaIntygPage extends Page {

    static at = { $("#about-mina-intyg-root").isDisplayed() }

    static content = {
        aboutSamtyckeLink(required: false) { $("#link-about-samtycke") }
        aboutSamtyckeSection(required: false) { $("#about-samtycke") }
        openRevokeConsentDialogLink(required: false) { $("#revokeConsentBtn") }
        
        
        revokeConsentDialog(required: false) { $("#revoke-consent-confirmation-dialog") }
        revokeConsentButton(required: false) { $("#revoke-consent-button") }
    }
    
    def gotoAboutConsentSection() {
        aboutSamtyckeLink.click()
    }
    
    def openRevokeConsentDialog() {
        openRevokeConsentDialogLink.click()
    }
    
    def revokeConsent() {
        revokeConsentButton.click()
    }

   
}
