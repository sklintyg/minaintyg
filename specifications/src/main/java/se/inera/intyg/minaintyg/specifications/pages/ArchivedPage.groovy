/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

class ArchivedPage extends AbstractLoggedInPage {

	static url = "start/#/arkiverade"

	static at = {doneLoading() && $("#archivedHeader").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
        archiveCertificateButton(required: false) { $("#archiveCertificateBtn") }
        viewCertificateButton(required: false) { $("#viewCertificateBtn") }
        confirmRestoreButton(required: false) { $("#restore-button") }
        restoreCertificateButton(required: false) { id -> $("#restoreCertificate-${id}") }
    }

    def restoreCertificate(String id) {
        restoreCertificateButton(id).click()
        waitFor {
            doneLoading()
        }
    }

    def confirmRestoreCertificate() {
        confirmRestoreButton.click()
        waitFor {
            doneLoading()
        }
    }

    def boolean certificateExists(String id) {
        restoreCertificateButton(id).isDisplayed()
    }
}
