/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.auth;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

import static org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFRAME_OPTIONS_HEADER;

/**
 * Adds Content-Security-Policy frame-ancestors if the request has a referer from the configured domain.
 *
 * Otherwise, a X-Frame-Options: DENY header is added.
 *
 * @author eriklupander
 */
@Component
public class CustomXFrameOptionsHeaderWriter implements HeaderWriter {

    private static final Logger LOG = LoggerFactory.getLogger(CustomXFrameOptionsHeaderWriter.class);

    private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    private static final String FRAME_ANCESTORS = "frame-ancestors ";
    private static final String DENY = "DENY";

    @Value("${csp.frameancestor.allow.from}")
    private String allowedDomain;

    @Value("${csp.frameancestor.allow.scheme}")
    private String allowedScheme;

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        if (Strings.isNullOrEmpty(referer)) {
            response.addHeader(XFRAME_OPTIONS_HEADER, "DENY");
            return;
        }

        try {
            URI uri = URI.create(referer);
            if (isExpectedScheme(uri) && hostIsExpectedDomain(uri)) {
                addContentSecurityPolicyHeader(response);
                return;
            }
        } catch (Exception ignored) {

        }
        // If the referer could not be parsed into an URI, did not match allowed scheme/domain
        // or there were some error, just add DENY header.
        response.addHeader(XFRAME_OPTIONS_HEADER, DENY);
    }

    // Adds three entries: Example: https://*.domain.com https://*.domain.com:* https://domain.com:*
    private void addContentSecurityPolicyHeader(HttpServletResponse response) {
        String subdomainAncestor = allowedScheme + "://*." + allowedDomain;
        String subdomainWithPortAncestor = allowedScheme + "://*." + allowedDomain + ":*";
        String withPortAncestor = allowedScheme + "://" + allowedDomain + ":*";

        response.addHeader(CONTENT_SECURITY_POLICY,
                FRAME_ANCESTORS + subdomainAncestor + " " + subdomainWithPortAncestor + " " + withPortAncestor);
    }

    private boolean hostIsExpectedDomain(URI uri) {
        return uri.getHost() != null && uri.getHost().endsWith(allowedDomain);
    }

    private boolean isExpectedScheme(URI uri) {
        return uri.getScheme() != null && uri.getScheme().equals(allowedScheme);
    }

    // Visible for testing.
    void setAllowedDomain(String allowedDomain) {
        this.allowedDomain = allowedDomain;
    }

    void setAllowedScheme(String allowedScheme) {
        this.allowedScheme = allowedScheme;
    }
}
