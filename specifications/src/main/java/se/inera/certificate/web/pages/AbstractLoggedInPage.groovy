package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

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
