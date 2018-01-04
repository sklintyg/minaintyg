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
package se.inera.intyg.minaintyg.web.controller.testability;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intyg.minaintyg.web.service.ConsentService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Transactional
@Api(value = "services anvandare", description = "REST API för testbarhet - Användare")
@Path("/anvandare")
public class UserConsentResource {

    @Autowired
    private ConsentService consentService;

    @GET
    @Path("/consent/give/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response giveConsent(@PathParam("userId") String userId) {
        boolean result = consentService.setConsent(new Personnummer(userId));
        if (result) {
            return Response.ok().build();
        }

        return Response.serverError().build();
    }

    @GET
    @Path("/consent/revoke/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response revokeConsent(@PathParam("userId") String userId) {
        boolean result = consentService.revokeConsent(new Personnummer(userId));
        if (result) {
            return Response.ok().build();
        }

        return Response.serverError().build();
    }

}
