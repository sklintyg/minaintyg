package se.inera.certificate.integration.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.inera.certificate.model.Utlatande;

/**
 * @author andreaskaltenbach
 */
public interface ModuleRestApi {

    /**
     * @param utlatande
     * @return
     */
    @POST
    @Path( "/valid" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    Response validate(Utlatande utlatande);

    /**
     * @param utlatande
     * @return
     */
    @POST
    @Path( "/pdf" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( "application/pdf" )
    Response pdf(Utlatande utlatande);

    /**
     * Convert from module-external format to module-internal format.
     *
     * @param utlatande
     * @return
     */
    @PUT
    @Path( "/internal" )
    @Consumes( MediaType.APPLICATION_JSON )
    Response convertExternalToInternal(Utlatande utlatande);

    /**
     * Convert from module-internal format to module-external format.
     *
     * @param utlatande
     * @return
     */
    @PUT
    @Path( "/external" )
    @Consumes( MediaType.APPLICATION_JSON )
    Response convertInternalToExternal(Object utlatande);
}
