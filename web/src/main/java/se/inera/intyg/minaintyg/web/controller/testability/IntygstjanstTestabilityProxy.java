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
package se.inera.intyg.minaintyg.web.controller.testability;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.Api;
import se.inera.intyg.clinicalprocess.healthcond.certificate.registerapprovedreceivers.v1.RegisterApprovedReceiversType;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.minaintyg.web.util.SystemPropertiesConfig;

@Api(value = "Proxy for intygstjanst testability API", description = "REST API för testbarhet - IT")
@Path("/resources")
public class IntygstjanstTestabilityProxy {


    public static final Logger LOG = LoggerFactory.getLogger(IntygstjanstTestabilityProxy.class);

    private final RestTemplate restTemplate;
    private final SystemPropertiesConfig systemPropertiesConfig;

    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    public IntygstjanstTestabilityProxy(RestTemplate restTemplate, SystemPropertiesConfig systemPropertiesConfig) {
        this.restTemplate = restTemplate;
        this.systemPropertiesConfig = systemPropertiesConfig;
    }

    @PostConstruct
    public void init() {
        headers.set("Content-Type", MediaType.APPLICATION_JSON);
    }

    @DELETE
    @Path("/certificate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteCertificate(@PathParam("id") String id) {
        restTemplate.delete(systemPropertiesConfig.getIntygstjanstBaseUrl() + "/inera-certificate/resources/certificate/" + id);
        return Response.ok().build();
    }

    @DELETE
    @Path("/certificate/citizen/{personId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteCertificatesForCitizen(@PathParam("personId") String personId) {
        restTemplate
                .delete(systemPropertiesConfig.getIntygstjanstBaseUrl() + "/inera-certificate/resources/certificate/citizen/" + personId);
        return Response.ok().build();
    }

    @POST
    @Path("/certificate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCertificate(CertificateHolder certificateHolder) {

        String url = systemPropertiesConfig.getIntygstjanstBaseUrl() + "/inera-certificate/resources/certificate";
        HttpEntity<CertificateHolder> entity = new HttpEntity<>(certificateHolder, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        return Response.ok().build();
    }

    @POST
    @Path("/certificate/{intygsId}/approvedreceivers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerApprovedReceivers(@PathParam("intygsId") String intygsId, RegisterApprovedReceiversType approvedReceiversType) {
        String url = systemPropertiesConfig.getIntygstjanstBaseUrl() + "/inera-certificate/resources/certificate/" + intygsId
                + "/approvedreceivers";

        HttpEntity<RegisterApprovedReceiversType> entity = new HttpEntity<>(approvedReceiversType, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        return Response.ok().build();
    }

    // returrns any resource from app env.
    @GET
    @Path("/resource")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response refData(@QueryParam("location") String location) throws IOException {
        LOG.info("GET resources/resource?location={}", location);
        return Response.ok(resourceLoader.getResource(location).getInputStream()).build();
    }
}
