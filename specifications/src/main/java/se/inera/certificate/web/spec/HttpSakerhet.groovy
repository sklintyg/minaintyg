package se.inera.certificate.web.spec

import groovyx.net.http.HttpResponseDecorator
import se.inera.certificate.web.spec.util.RestClientFixture

import static groovyx.net.http.ContentType.JSON

class HttpSakerhet  extends RestClientFixture {

    def client

    void loggaPåSom(String pnr) {
        client = createRestClient()
        client.get(path: "/web/sso", query: [guid : pnr] )
    }

    public void geSamtycke() {
        client.post(path: "/api/certificates/consent/give", body: "{}", requestContentType: JSON, headers: [Accept:"application/json"])
    }

    String hamtaHeader(url, header) {
        HttpResponseDecorator response = client.get(path: url)
        response.getHeaders(header)[0].value
    }

    boolean apiSvarSkaEjKunnaCachas() {
        hamtaHeader("/api/certificates/map", "Cache-Control") == "no-cache, no-store, max-age=0, must-revalidate"
    }

    boolean modulApiSvarSkaEjKunnaCachas() {
        hamtaHeader("/moduleapi/certificate/intyg-1", "Cache-Control") == "no-cache, no-store, max-age=0, must-revalidate"
    }

    boolean enSidaSkaInteKunnaSattasIFrame() {
        hamtaHeader("http://localhost:8088/web/start", "X-Frame-Options") == "DENY"
    }

}
