package se.inera.certificate.integration.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.inera.certificate.common.v1.Utlatande;

/**
 * @author andreaskaltenbach
 */
public interface ModuleRestApi {

    @POST
    @Path("/extension")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    Object extract(Utlatande utlatande);


    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    Response validate(Utlatande utlatande);

    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    Response pdf(Utlatande intyg);
}
