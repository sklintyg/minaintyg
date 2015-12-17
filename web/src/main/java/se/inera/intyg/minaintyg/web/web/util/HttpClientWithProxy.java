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

package se.inera.intyg.minaintyg.web.web.util;

import org.apache.commons.httpclient.HttpClient;

public final class HttpClientWithProxy {

    private HttpClientWithProxy() {
    }

    public static HttpClient create() {
        HttpClient httpClient = new HttpClient();

        String proxyHost = System.getProperty("https.proxyHost");
        if (proxyHost != null) {
            String proxyPort = System.getProperty("https.proxyPort", "-1");
            httpClient.getHostConfiguration().setProxy(proxyHost, Integer.parseInt(proxyPort));
        }
        return httpClient;
    }
}
