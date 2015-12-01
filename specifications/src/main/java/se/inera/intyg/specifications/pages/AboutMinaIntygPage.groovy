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
        waitFor {
            doneLoading()
        }
    }

    def openRevokeConsentDialog() {
        openRevokeConsentDialogLink.click()
        waitFor {
            doneLoading()
        }
    }

    def revokeConsent() {
        revokeConsentButton.click()
        waitFor {
            doneLoading()
        }
    }
}
