package se.inera.certificate.integration.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.inera.certificate.model.Utlatande;

/**
 * @author andreaskaltenbach
 */
public interface ModuleRestApi {

    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response validate(Utlatande utlatande);

    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    Response pdf(Utlatande utlatande);
}
