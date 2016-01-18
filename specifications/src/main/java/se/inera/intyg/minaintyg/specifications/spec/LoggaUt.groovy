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
import se.inera.intyg.minaintyg.specifications.pages.AccessDeniedPage
import se.inera.intyg.minaintyg.specifications.pages.MvkLoginPage
import se.inera.intyg.minaintyg.specifications.pages.MvkLogoutPage

public class LoggaUt extends AbstractWebFixture {

    public void tillbakaTillMvk() {
        Browser.drive {
            page.goBackToMvk()
        }
    }

    public void loggaUt() {
        Browser.drive {
            page.logout()
        }
    }

    public boolean mvksInloggningssidaVisas() {
        boolean result
        Browser.drive {
            result = at MvkLoginPage
        }
        return result
    }

    public boolean mvksUtloggningssidaVisas() {
        boolean result
        Browser.drive {
            result = at MvkLogoutPage
        }
        return result
    }
}
