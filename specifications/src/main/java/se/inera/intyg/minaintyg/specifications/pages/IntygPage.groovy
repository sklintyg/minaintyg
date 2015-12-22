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


class IntygPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#certificate").isDisplayed() }

    static content = {
        certificateId { $("#certId") }
        certificateType { $("#certType") }
        sendCertificateBtn(to: SendCertificateChooseRecipientPage, toWait: true) { $("#sendCertificateBtn") }
        latestCertificateEvent { $("#latest-certificate-event") }
        archiveBtn { $("#archiveBtn") }
        confirmArchiveBtn(required: false) { $("#archive-button") }
    }

    def startSendFlow() {
        sendCertificateBtn.click()
    }

    def archive() {
        archiveBtn.click()
        waitFor {
            doneLoading()
        }
    }

    def confirmArchiveCertificate() {
        confirmArchiveBtn.click()
        // TODO: FIX!! The animation on InboxPage requires delay, otherwise doneLoading() returns true immediately
        Thread.sleep(1000)
        waitFor {
            doneLoading()
        }
    }
    
    boolean hasStatus(String status) {
        latestCertificateEvent.any { it.text().contains(status) }
    }
}
