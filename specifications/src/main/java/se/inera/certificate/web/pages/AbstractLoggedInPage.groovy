package se.inera.certificate.web.pages

import se.inera.certificate.page.AbstractPage

class AbstractLoggedInPage extends AbstractPage {

    static content = {
        backToMvkLink { $("#backToMvkLink") }
        logoutLink { $("#mvklogoutLink") }
    }
    
    def goBackToMvk() {
        backToMvkLink.click()
    }

    def logout() {
        logoutLink.click()
    }
}
