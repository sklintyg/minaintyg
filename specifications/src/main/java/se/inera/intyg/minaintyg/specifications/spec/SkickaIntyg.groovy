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

public class SkickaIntyg {

    public boolean väljMottagareVynVisas() {
        boolean result
        Browser.drive {
            result = page.confirmRecipientSelectionBtn?.isDisplayed()
        }
        return result
    }

    public void väljMottagare(String mottagare) {
        Browser.drive {
            page.chooseRecipient(mottagare)
        }
    }

    public void bekräftaValdMottagare() {
        Browser.drive {
            page.confirmRecipientSelection()
        }
    }

    public boolean summeringsVynVisas() {
        boolean result
        Browser.drive {
            result = page.confirmAndSendBtn?.isDisplayed()
        }
        return result
    }
    
    public boolean valdMottagareÄr(String mottagare) {
        boolean result
        Browser.drive {
            result = page.selectedRecipientMessage.text() == mottagare
        }
        return result
    }

    public void bekräftaSkickaIntyg() {
        Browser.drive {
            page.confirmSend()
        }
    }

    public boolean resultatSidanVisas() {
        boolean result
        Browser.drive {
            result = page.resultMessageContainer?.isDisplayed()
        }
        return result
    }

    public boolean redanSkickatVarningVisas() {
        boolean result
        Browser.drive {
            result = page.alreadySentWarningMessage?.isDisplayed()
        }
        return result
    }
    
    public void gåTillbakaTillIntyget() {
        Browser.drive {
            page.backToCertificateView()
        }
    }

}
