package se.inera.certificate.integration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author andreaskaltenbach
 */
public interface IneraCertificateRestApi {

    @GET
    @Path("/certificate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    String getCertificate(@PathParam("id") String certificateId);

    @GET
    @Path("/certificate/{id}")
    @Produces("application/pdf")
    byte[] getCertificatePdf(@PathParam("id") String certificateId);
}
