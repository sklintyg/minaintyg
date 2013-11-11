package se.inera.certificate.web.pages

import geb.Page

class AboutMinaIntygPage extends Page {

    static at = { $("#about-mina-intyg-root").isDisplayed() }

    static content = {
        aboutSamtyckeLink(required: false) { $("#link-about-samtycke") }
        aboutSamtyckeSection(required: false) { $("#about-content-samtycke") }
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
        // Since this dialog is animated, wait for the same time as the animation
        // in order to avoid problem with Chrome WebDriver and moving click targets
        Thread.sleep(300);
        revokeConsentButton.click()
    }

   
}
