/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.appconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.modules.registry.IntygModule;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.infra.dynamiclink.service.DynamicLinkService;
import se.inera.intyg.infra.integration.ia.services.IABannerService;
import se.inera.intyg.minaintyg.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.minaintyg.web.util.SystemPropertiesConfig;

/**
 * Created by marced on 2017-05-12.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigApiControllerTest {

    private static final String BUILD_NUMBER = "1.0";
    private static final String ELVA77_MAIN_URL = "http://mvk.se/ABC123?apa=bepa";
    private static final String ELVA77_LOGIN_URL = "/saml/login";
    private static final String APP_LOGOUT_URL = "/web/logga-ut";
    private static final java.lang.String VERSION = "1.2.3";

    @Mock
    private IntygModuleRegistry moduleRegistry;

    @Mock
    private CertificateService certificateService;

    @Mock
    private SystemPropertiesConfig systemConfigBean;

    @Mock
    private DynamicLinkService dynamicLinkService;

    @Mock
    private IABannerService iaBannerService;

    @InjectMocks
    private ConfigApiController controller;

    @Test
    public void getConfig() throws Exception {
        when(certificateService.getAllRecipients()).thenReturn(Arrays.asList(mock(UtlatandeRecipient.class)));
        when(systemConfigBean.getVersion()).thenReturn(VERSION);
        when(systemConfigBean.getBuildNumber()).thenReturn(BUILD_NUMBER);
        when(systemConfigBean.getElva77MainUrl()).thenReturn(ELVA77_MAIN_URL);
        when(systemConfigBean.getElva77LoginUrl()).thenReturn(ELVA77_LOGIN_URL);
        when(systemConfigBean.getApplicationLogoutUrl()).thenReturn(APP_LOGOUT_URL);
        when(systemConfigBean.getUseMinifiedJavascript()).thenReturn(false);
        when(dynamicLinkService.getAllAsMap()).thenReturn(new HashMap<>());
        when(iaBannerService.getCurrentBanners()).thenReturn(new ArrayList<>());

        Response response = controller.getConfig();

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        ConfigResponse config = (ConfigResponse) response.getEntity();

        assertNotNull(config);
        assertEquals(1, config.getKnownRecipients().size());
        assertEquals(VERSION, config.getVersion());
        assertEquals(BUILD_NUMBER, config.getBuildNumber());
        assertEquals(ELVA77_MAIN_URL, config.getElva77MainUrl());
        assertEquals(ELVA77_LOGIN_URL, config.getElva77LoginUrl());
        assertEquals(APP_LOGOUT_URL, config.getApplicationLogoutUrl());
        assertEquals(false, config.isUseMinifiedJavascript());

        verify(certificateService).getAllRecipients();
    }

    @Test
    public void testGetModulesMap() throws Exception {
        when(moduleRegistry.listAllModules()).thenReturn(Arrays.asList(mock(IntygModule.class)));

        Response response = controller.getModulesMap();

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        List<IntygModule> res = (List<IntygModule>) response.getEntity();

        assertNotNull(res);
        assertEquals(1, res.size());

        verify(moduleRegistry).listAllModules();
    }

}
