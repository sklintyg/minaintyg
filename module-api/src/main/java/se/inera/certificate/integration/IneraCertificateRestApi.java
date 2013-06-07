package se.inera.certificate.integration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST API exposed by the inera-certificate application aka intygstjänsten.
 *
 * @author andreaskaltenbach
 */
public interface IneraCertificateRestApi {

    @GET
    @Path( "/certificate/{id}" )
    @Produces("application/pdf")
    Response getCertificatePdf(@PathParam( "id" ) String certificateId);

    @GET
    @Path( "/certificate/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getCertificate(@PathParam( "id" ) String certificateId);
}
