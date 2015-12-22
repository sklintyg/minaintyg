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

import se.inera.intyg.common.specifications.page.AbstractPage
import se.inera.intyg.common.specifications.spec.Browser
import se.inera.intyg.minaintyg.specifications.pages.AboutMinaIntygPage
import se.inera.intyg.minaintyg.specifications.pages.AccessDeniedPage
import se.inera.intyg.minaintyg.specifications.pages.ArchivedPage
import se.inera.intyg.minaintyg.specifications.pages.ConsentPage
import se.inera.intyg.minaintyg.specifications.pages.InboxPage

public class AbstractWebFixture {

    public void loggaPåSom(String pnr) {
        Browser.drive {
            go "sso?guid=${pnr}"
            waitFor {
                AbstractPage.doneLoading()
            }
        }
    }

    public boolean inkorgsidanVisas() {
        boolean result
        Browser.drive {
            result = at InboxPage
        }
        return result
    }

    public boolean arkiveradesidanVisas() {
        boolean result
        Browser.drive {
            result = at ArchivedPage
        }
        return result
    }

    public boolean geSamtyckesidanVisas() {
        boolean result
        Browser.drive {
            result = at ConsentPage
        }
    }

    public boolean omMinaIntygsidanVisas() {
        boolean result
        Browser.drive {
            result = at AboutMinaIntygPage
        }
        return result
    }

    public void gåTillStartsida() {
        Browser.drive {
            go "/web/start"
            waitFor {
                AbstractPage.doneLoading()
            }
        }
    }

    public boolean accessDeniedVisas() {
        boolean result
        Browser.drive {
            result = at AccessDeniedPage
        }
        return result
    }

    public void laddaOm() {
        Browser.drive {
            getJs().exec([], "window.location.reload()")
            waitFor {
                AbstractPage.doneLoading()
            }
        }
    }
    
    public void besökExternSida() {
        Browser.drive {
            go "http://www.google.com"
            waitFor {
                title == "Google"
            }
        }
    }

    public void väntaSekunder(int sekunder) {
        Thread.sleep(sekunder * 1000)
    }
}
