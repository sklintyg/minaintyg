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

package se.inera.intyg.minaintyg.web.util;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.infra.security.common.model.AuthConstants.ALIAS_ELEG;
import static se.inera.intyg.infra.security.common.model.AuthConstants.ALIAS_WWW_ELEG;

import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemPropertiesConfigTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private SystemPropertiesConfig systemPropertiesConfig;

    private static final String WWW_SERVER_NAME = "wWw.mi.test.se";
    private static final String NON_WWW_SERVER_NAME = "mi.test.se";

    @Test
    public void shouldSetUrlAliasForWWWWhenCurrentLocationIsWWW() {
        when(httpServletRequest.getServerName()).thenReturn(WWW_SERVER_NAME);

        final var loginUrl = systemPropertiesConfig.getElva77LoginUrl();

        assertTrue(loginUrl.endsWith(ALIAS_WWW_ELEG));
    }

    @Test
    public void shouldSetUrlAliasForNonWWWWhenCurrentLocationIsNonWWW() {
        when(httpServletRequest.getServerName()).thenReturn(NON_WWW_SERVER_NAME);

        final var loginUrl = systemPropertiesConfig.getElva77LoginUrl();

        assertTrue(loginUrl.endsWith(ALIAS_ELEG));
    }
}
