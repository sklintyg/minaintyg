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


class SendCertificateSummaryPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#send-certificate-flow-root-summary").isDisplayed() }

    static content = {
        confirmAndSendBtn(required: false, to: SendCertificateSentPage, toWait: true) { $("#confirmAndSendBtn") }
        backToViewRecipentsBtn(required: false, to: SendCertificateChooseRecipientPage, toWait: true)  { $("#backToViewRecipentsBtn") }
        cancelSendBtn(required: false, to: IntygPage, toWait: true)  { $("#cancelSendBtn") }
        selectedRecipientMessage(required: false)  { $("#selected-recipient-message") }
        alreadySentWarningMessage(required: false)  { $("#already-sent-to-recipient-message") }
    }

    def confirmSend() {
        confirmAndSendBtn.click()
    }

    def backToViewRecipents() {
        backToViewRecipentsBtn.click()
    }

    def cancelSend() {
        cancelSendBtn.click()
    }
}
