/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * @author andreaskaltenbach
 */
public class FakeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(FakeAuthenticationFilter.class);

    protected FakeAuthenticationFilter() {
        super("/web/sso");
        LOG.error("FakeAuthentication enabled. DO NOT USE IN PRODUCTION");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }

        String guid = request.getParameter("guid");
        String origin = request.getParameter("origin") != null && request.getParameter("origin").trim().length() > 0
            ? request.getParameter("origin")
            : "ELVA77";

        // we manually encode the json guid
        String json = URLDecoder.decode(guid, StandardCharsets.UTF_8);
        return performFakeElegAuthentication(json, origin);
    }

    private Authentication performFakeElegAuthentication(String personnummer, String origin) {
        FakeElegCredentials fakeElegCredentials = new FakeElegCredentials();
        fakeElegCredentials.setPersonId(personnummer);
        fakeElegCredentials.setOrigin(origin);
        LOG.info("Detected fake credentials " + fakeElegCredentials);
        return getAuthenticationManager()
            .authenticate(new FakeElegAuthenticationToken(fakeElegCredentials));
    }
}
