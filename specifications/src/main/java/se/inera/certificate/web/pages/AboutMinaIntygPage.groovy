package se.inera.certificate.web.pages

class AboutMinaIntygPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#about-mina-intyg-root").isDisplayed() }

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
        waitFor {
            doneLoading()
        }
        revokeConsentButton.click()
    }
}
