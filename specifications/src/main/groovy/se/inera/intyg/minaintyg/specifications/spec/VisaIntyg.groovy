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

package se.inera.intyg.minaintyg.specifications.spec
import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.minaintyg.specifications.pages.IntygFk7263Page
import se.inera.intyg.minaintyg.specifications.pages.IntygPage
import se.inera.intyg.minaintyg.specifications.pages.IntygTsBasPage
import se.inera.intyg.minaintyg.specifications.pages.IntygTsDiabetesPage

public class VisaIntyg {

    public boolean intygssidanVisas() {
        boolean result
        Browser.drive {
            result = at IntygPage
        }
        return result
    }

    public boolean intygetsTypÄr(String typ) {
        boolean result
        Browser.drive {
            switch (typ) {
            case "fk7263":
                result = at IntygFk7263Page
                break
            case "ts-bas":
                result = at IntygTsBasPage
                break
            case "ts-diabetes":
                result = at IntygTsDiabetesPage
                break
            default:
                result = false
            }
        }
        result
    }

    public boolean intygetsIdÄr(String id) {
        boolean result
        Browser.drive {
            result = $("#certId", title: id).size() == 1
        }
        result
    }

    public void väljSkickaIntyg() {
        Browser.drive {
            page.startSendFlow()
        }
    }

    public void väljArkiveraIntyg() {
        Browser.drive {
            page.archive();
        }
    }

    public void konfirmeraArkiveraIntyg() {
        Browser.drive {
            page.confirmArchiveCertificate()
        }
    }

    public boolean intygetHarEnStatusTextInnehållande(String textFragment) {
        boolean result
        Browser.drive {
            result = page.hasStatus(textFragment)
        }
        result
    }
}
