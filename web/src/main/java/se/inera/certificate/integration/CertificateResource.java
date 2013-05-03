package se.inera.certificate.integration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.transaction.annotation.Transactional;

import se.inera.certificate.model.Certificate;

/**
 * @author andreaskaltenbach
 */
@Path("/certificate")
public class CertificateResource {

    @PersistenceContext
    private EntityManager entityManager;
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Certificate getCertificate(@PathParam("id") String id) {
        return entityManager.find(Certificate.class, id);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteCertificate(@PathParam("id") String id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        entityManager.remove(certificate);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response insertCertificate(Certificate certificate) {
        entityManager.persist(certificate);
        return Response.ok().build();
    }
}
