package se.inera.certificate.integration;


import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Valideringsresultat;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author andreaskaltenbach
 */
public interface ModuleRestApi {

    @POST
    @Path("/extension")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    Ovrigt extract (Lakarutlatande lakarutlatande);

    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    Valideringsresultat validate (Lakarutlatande lakarutlatande);
}
