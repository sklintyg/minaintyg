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

import se.inera.intyg.common.specifications.page.AbstractPage

class InboxPage extends AbstractLoggedInPage {

    static url = "start/#/lista"

    static at = { doneLoading() && $("#inboxHeader").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
        confirmArchiveButton(required: false) { $("#archive-button") }
        certificate(required: false) { id -> $("#certificate-${id}") }
        archiveCertificateButton(required: false) { id -> $("#archiveCertificateBtn-${id}") }
        viewCertificateButton(required: false, to: IntygPage, toWait: true) { id -> $("#viewCertificateBtn-${id}") }
        complementaryInfoText(required: false) { id -> $("#certificate-period-${id}")}
    }

    def archiveCertificate(String id) {
        archiveCertificateButton(id).click()
        waitFor {
            doneLoading()
        }
    }

    def confirmArchiveCertificate() {
        confirmArchiveButton.click()
        // TODO: FIX!! The animation on InboxPage requires delay, otherwise doneLoading() returns true immediately
        Thread.sleep(1000)
        waitFor {
            doneLoading()
        }
    }

    def boolean certificateExists(String id) {
        certificate(id).isDisplayed();
    }

    def viewCertificate(String id) {
        viewCertificateButton(id).click()
    }

    def boolean cancelledCertificateDisplayed(String id) {
        def viewCertButton = viewCertificateButton(id)
        !viewCertButton.isDisplayed() || viewCertButton.isDisabled();
    }

    def String complementaryInfoIsSet(String id) {
        complementaryInfoText(id).text()
    } 
}
