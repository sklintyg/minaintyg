package se.inera.intyg.specifications.pages

class HelpAndSupportPage extends AbstractLoggedInPage {

    static at = { doneLoading() && $("#help-mina-intyg-root").isDisplayed() }

    static content = {
        linkHelpDescription { $("#link-help-description") }
        linkHelpFaq { $("#link-help-faq") }
    }
}
