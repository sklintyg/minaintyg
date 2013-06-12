package se.inera.certificate.integration.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.inera.certificate.integration.v1.Lakarutlatande;

/**
 * @author andreaskaltenbach
 */
public interface ModuleRestApi {

    @POST
    @Path("/extension")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    Object extract(Lakarutlatande lakarutlatande);


    @POST
    @Path( "/valid" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.TEXT_PLAIN )
    Response validate(Lakarutlatande intyg);

    @POST
    @Path( "/pdf" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( "application/pdf" )
    Response pdf(se.inera.certificate.model.Lakarutlatande intyg);
}
