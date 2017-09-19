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
package se.inera.intyg.minaintyg.web.controller.appconfig;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.infra.dynamiclink.service.DynamicLinkService;
import se.inera.intyg.minaintyg.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.util.SystemPropertiesConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @Autowired
    private DynamicLinkService dynamicLinkService;

    @GET
    @Path("/app")
    @Produces(JSON_UTF8)
    public Response getConfig() {

        Response.ResponseBuilder builder = Response
                .ok(new ConfigResponse(systemConfigBean.getBuildNumber(), systemConfigBean.getUseMinifiedJavascript(),
                        systemConfigBean.getElva77MainUrl(), systemConfigBean.getElva77LoginUrl(), certificateService.getAllRecipients(),
                        dynamicLinkService.getAllAsMap()));
        return builder.cacheControl(getNoCacheControl()).build();
    }

    /**
     * Serving module configuration for Angular bootstrapping.
     *
     * @return a JSON object
     */
    @GET
    @Path("/map")
    @Produces(JSON_UTF8)
    public Response getModulesMap() {
        Response.ResponseBuilder builder = Response
                .ok(moduleRegistry.listAllModules());
        return builder.cacheControl(getNoCacheControl()).build();
    }

    private CacheControl getNoCacheControl() {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setNoStore(true);
        cc.setMustRevalidate(true);
        cc.setPrivate(true);
        cc.setMaxAge(0);

        return cc;
    }
}
