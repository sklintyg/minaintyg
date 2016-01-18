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

import groovyx.net.http.HttpResponseDecorator
import se.inera.intyg.minaintyg.specifications.spec.util.RestClientFixture

import static groovyx.net.http.ContentType.JSON

class HttpSakerhet  extends RestClientFixture {

    def client = createRestClient(baseUrl)

    void loggaPåSom(String pnr) {
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
        hamtaHeader("/moduleapi/certificate/fit-intyg-1", "Cache-Control") == "no-cache, no-store, max-age=0, must-revalidate"
    }

    boolean enSidaSkaInteKunnaSattasIFrame() {
        hamtaHeader("http://localhost:8088/web/start", "X-Frame-Options") == "DENY"
    }

}
