/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

import static org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFRAME_OPTIONS_HEADER;

/**
 * Adds Content-Security-Policy frame-ancestors and X-Frame-Options: ALLOW-FROM headers
 * if the request has a referer from the configured domain.
 *
 * @author eriklupander
 */
@Component
public class CustomXFrameOptionsHeaderWriter implements HeaderWriter {

    private static final Logger LOG = LoggerFactory.getLogger(CustomXFrameOptionsHeaderWriter.class);

    @Value("${csp.frameancestor.allow.from}")
    private String allowFrom;

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        if (Strings.isNullOrEmpty(referer)) {
            return;
        }
        if (referer.startsWith("https://") && referer.contains(allowFrom)) {
            response.addHeader("Content-Security-Policy", "frame-ancestors " + referer);
            response.addHeader(XFRAME_OPTIONS_HEADER, "ALLOW-FROM " + referer);
        }
    }
}
