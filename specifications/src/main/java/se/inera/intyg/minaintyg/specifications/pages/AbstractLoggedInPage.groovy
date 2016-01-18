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

class AbstractLoggedInPage extends AbstractPage {

    static at = { doneLoading() && $("#mvklogoutLink").isDisplayed() }

    static content = {
        backToMvkLink(to: MvkLoginPage, toWait: true) { $("#backToMvkLink") }
        logoutLink(to: MvkLogoutPage, toWait: true) { $("#mvklogoutLink") }
        inboxTab(to: InboxPage, toWait: true) { $("#inboxTab") }
        archivedTab(to: ArchivedPage, toWait: true) { $("#archivedTab") }
        aboutTab(to: AboutMinaIntygPage, toWait: true) { $("#aboutTab") }
        helpAndSupportTab(to: HelpAndSupportPage, toWait: true) { $("#helpTab") }
    }

    def goBackToMvk() {
        backToMvkLink.click()
    }

    def logout() {
        logoutLink.click()
    }

    def goToInboxPage() {
        AbstractPage.scrollIntoView("inboxTab")
        inboxTab.click()
    }

    def goToArchivedPage() {
        AbstractPage.scrollIntoView("archivedTab")
        archivedTab.click()
    }

    def goToAboutPage() {
        AbstractPage.scrollIntoView("aboutTab")
        aboutTab.click()
    }

    def goToHelpAndSupportPage() {
        AbstractPage.scrollIntoView("helpTab")
        helpAndSupportTab.click()
    }

    def activeTab() {
        $('.navbar-nav li.active > a').getAt(0).getAttribute('id')
    }
}
