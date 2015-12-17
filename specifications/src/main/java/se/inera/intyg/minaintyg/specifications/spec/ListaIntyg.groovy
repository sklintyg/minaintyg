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

package se.inera.intyg.minaintyg.specifications.spec

import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.minaintyg.specifications.pages.ArchivedPage
import se.inera.intyg.minaintyg.specifications.pages.InboxPage

public class ListaIntyg extends AbstractWebFixture {

    public boolean listaMedIntygVisas() {
        boolean result
        Browser.drive {
            result = page.certificateTable.isDisplayed()
        }
        return result
    }

    public boolean listaMedArkiveradeIntygVisas() {
        boolean result
        Browser.drive {
            result = page.certificateTable.isDisplayed()
        }
        return result
    }

    public boolean finnsIngaIntyg() {
        boolean result
        Browser.drive {
            result = page.noCertificates.isDisplayed()
        }
        return result
    }

    public void arkiveraIntyg(String id) {
        Browser.drive {
            page.archiveCertificate(id)
        }
    }

    public void konfirmeraArkiveraIntyg() {
        Browser.drive {
            page.confirmArchiveCertificate()
        }
    }

    public void återställIntyg(String id) {
        Browser.drive {
            page.restoreCertificate(id)
        }
    }

    public void konfirmeraÅterställIntyg() {
        Browser.drive {
            page.confirmRestoreCertificate()
        }
    }

    public void gåTillArkiveradeIntyg() {
        Browser.drive {
            page.goToArchivedPage()
        }
    }

    public void gåTillInkorgen() {
        Browser.drive {
            page.goToInboxPage()
        }
    }

    public boolean arkiveratIntygFinnsIListan(String id) {
        boolean result
        Browser.drive {
            result = page.certificateExists(id)
        }
        return result
    }

    public boolean intygFinnsIListan(String id) {
        boolean result
        Browser.drive {
            result = page.certificateExists(id)
        }
        return result
    }

    public boolean rättatIntygVisasKorrekt(String id) {
        boolean result
        Browser.drive {
            result = page.cancelledCertificateDisplayed(id);
        }
        return result
    }

    public void visaIntyg(String id) {
        Browser.drive {
            page.viewCertificate(id)
        }
    }

    public boolean complementaryInfoArSatt(String id) {
        boolean result
        Browser.drive {
            result = !page.complementaryInfoIsSet(id).isEmpty()
        }
        return result
    }
}
