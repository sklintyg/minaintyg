package se.inera.certificate.web.pages

import geb.Page

class InboxPage extends Page {

	static url = "start#/lista"
 
	static at = { $("h1 span[key='certificates.header']").isDisplayed() }

    static content = {
        certificateTable(required: false) { $("#certTable") }
        noCertificates(required: false) { $("#noCerts") }
    }

}
