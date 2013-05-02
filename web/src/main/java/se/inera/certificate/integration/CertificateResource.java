package se.inera.certificate.integration;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author andreaskaltenbach
 */
@Path("/certificate")
public class CertificateResource {

    @Autowired
    private CertificateService certificateService;

    @GET
    @Path("/{id}")
    @Produces( MediaType.APPLICATION_JSON )
    public Certificate getCertificate(@PathParam("id") String id) {
        return certificateService.getCertificate(null, id);
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response insertCertificate(Certificate certificate) {
        certificateService.storeCertificate(certificate);
        return Response.ok().build();
    }
}
