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
package se.inera.intyg.minaintyg.web.web.controller.appconfig;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.modules.registry.IntygModule;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.minaintyg.web.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.web.util.SystemPropertiesConfig;

/**
 * Created by marced on 2017-05-12.
 */
public class ConfigApiController {

    private static final String CHARSET_UTF_8 = ";charset=utf-8";
    private static final String JSON_UTF8 = MediaType.APPLICATION_JSON + CHARSET_UTF_8;

    @Autowired
    private IntygModuleRegistry moduleRegistry;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private SystemPropertiesConfig systemConfigBean;

    @GET
    @Path("/app")
    @Produces(JSON_UTF8)
    public ConfigResponse getConfig() {
        return new ConfigResponse(systemConfigBean.getBuildNumber(), systemConfigBean.getUseMinifiedJavascript(),
                systemConfigBean.getMvkMainUrl(), certificateService.getAllRecipients());
    }

    /**
     * Serving module configuration for Angular bootstrapping.
     *
     * @return a JSON object
     */
    @GET
    @Path("/map")
    @Produces(JSON_UTF8)
    public List<IntygModule> getModulesMap() {
        return moduleRegistry.listAllModules();
    }
}
