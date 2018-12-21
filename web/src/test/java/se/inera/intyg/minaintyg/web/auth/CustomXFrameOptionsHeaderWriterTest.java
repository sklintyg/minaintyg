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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomXFrameOptionsHeaderWriterTest {

    private HttpServletRequest req = mock(HttpServletRequest.class);
    private HttpServletResponse resp = mock(HttpServletResponse.class);

    private CustomXFrameOptionsHeaderWriter testee = new CustomXFrameOptionsHeaderWriter();

    private static final String REFERER = "https://m00.local.idp.funktionstjanster.se/some/kind/of/path";

    @Before
    public void init() {
        testee.setAllowedDomain("funktionstjanster.se");
        testee.setAllowedScheme("https");
    }

    @Test
    public void parseReferer() {
        when(req.getHeader("Referer")).thenReturn(REFERER);
        testee.writeHeaders(req, resp);
        verify(resp).addHeader("Content-Security-Policy",
                "frame-ancestors https://*.funktionstjanster.se https://*.funktionstjanster.se:* https://funktionstjanster.se:*");
    }

    @Test
    public void verifyDENYWhenNoReferer() {
        when(req.getHeader("Referer")).thenReturn(null);
        testee.writeHeaders(req, resp);
        verify(resp).addHeader("X-Frame-Options", "DENY");
    }

    @Test
    public void verifyDENYWhenRefererDoesNotMatchFunktionstjanster() {
        when(req.getHeader("Referer")).thenReturn("http://evil.domain.nu");
        testee.writeHeaders(req, resp);
        verify(resp).addHeader("X-Frame-Options", "DENY");
    }

    @Test
    public void verifyDENYWhenMalformedReferer() {
        when(req.getHeader("Referer")).thenReturn("httxp://e:vil.domain.nu");
        testee.writeHeaders(req, resp);
        verify(resp).addHeader("X-Frame-Options", "DENY");
    }
}
