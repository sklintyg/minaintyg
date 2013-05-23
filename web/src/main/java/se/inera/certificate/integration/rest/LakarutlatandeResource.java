package se.inera.certificate.integration.rest;

import se.inera.certificate.model.Lakarutlatande;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeResource {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Lakarutlatande getLakarutlatande(@PathParam("id") String id) {
        return new Lakarutlatande();
    }
}
