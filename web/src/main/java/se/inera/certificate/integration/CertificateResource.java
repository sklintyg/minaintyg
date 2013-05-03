package se.inera.certificate.integration;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;

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

    @DELETE
    @Path("/{id}")
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteCertificate(@PathParam("id") String id) {
		certificateService.remove(id);
		return Response.ok().build();
	}
    
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Path("/")
    public Response insertCertificate(Certificate certificate) {
    	System.out.println(certificate);
        certificateService.storeCertificate(certificate);
        return Response.ok().build();
    }
}
