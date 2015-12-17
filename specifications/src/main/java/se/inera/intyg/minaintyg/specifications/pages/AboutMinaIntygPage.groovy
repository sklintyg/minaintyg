/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.minaintyg.specifications.pages

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
