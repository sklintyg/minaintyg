package se.inera.intyg.minaintyg.web.web.controller.appconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.modules.registry.IntygModule;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.minaintyg.web.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.minaintyg.web.web.util.SystemPropertiesConfig;

/**
 * Created by marced on 2017-05-12.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigApiControllerTest {

    private static final String BUILD_NUMBER = "1.0";
    private static final String MVK_URL = "http://mvk.se/ABC123?apa=bepa";

    @Mock
    private IntygModuleRegistry moduleRegistry;

    @Mock
    private CertificateService certificateService;

    @Mock
    private SystemPropertiesConfig systemConfigBean;

    @InjectMocks
    private ConfigApiController controller;

    @Test
    public void getConfig() throws Exception {
        when(certificateService.getAllRecipients()).thenReturn(Arrays.asList(mock(UtlatandeRecipient.class)));
        when(systemConfigBean.getBuildNumber()).thenReturn(BUILD_NUMBER);
        when(systemConfigBean.getMvkMainUrl()).thenReturn(MVK_URL);
        when(systemConfigBean.getUseMinifiedJavascript()).thenReturn(false);

        ConfigResponse config = controller.getConfig();

        assertNotNull(config);
        assertEquals(1, config.getKnownRecipients().size());
        assertEquals(BUILD_NUMBER, config.getBuildNumber());
        assertEquals(MVK_URL, config.getMvkMainUrl());
        assertEquals(false, config.isUseMinifiedJavascript());

        verify(certificateService).getAllRecipients();
    }

    @Test
    public void testGetModulesMap() throws Exception {
        when(moduleRegistry.listAllModules()).thenReturn(Arrays.asList(mock(IntygModule.class)));

        List<IntygModule> res = controller.getModulesMap();

        assertNotNull(res);
        assertEquals(1, res.size());

        verify(moduleRegistry).listAllModules();
    }

}
