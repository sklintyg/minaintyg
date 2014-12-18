package se.inera.certificate.web.spec

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import se.inera.certificate.web.pages.InboxPage

class HttpSakerhet {
    void loggaPåSom(String pnr) {
        Browser.drive {
            go "sso?guid=${pnr}"
        }
    }

    boolean inkorgsidanVisas() {
        Browser.drive {
            waitFor {
                at InboxPage
            }
        }
    }

    String hamtaHeader(url, header) {
        def client = new RESTClient("http://localhost:8088/")
        def headers = new HashMap<String,String>()
        headers.put("Cookie","JSESSIONID=" + Browser.getJSession())
        HttpResponseDecorator response = client.get(path: url, headers:headers)
        response.getHeaders(header)[0].value
    }

    boolean apiSvarSkaEjKunnaCachas() {
        hamtaHeader("/api/certificates/map", "Cache-Control") == "no-cache, no-store, max-age=0, must-revalidate"
    }

    boolean modulApiSvarSkaEjKunnaCachas() {
        hamtaHeader("/moduleapi/certificate/fit-intyg-1", "Cache-Control") == "no-cache, no-store, max-age=0, must-revalidate"
    }

    boolean enSidaSkaInteKunnaSattasIFrame() {
        hamtaHeader("http://localhost:8088/web/start", "X-Frame-Options") == "DENY"
    }

}
