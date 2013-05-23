package se.inera.certificate.integration.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Patient;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeResource implements IneraCertificateRestApi {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new LakarutlatandeJacksonModule());
    }

    @Override
    public String getCertificate(String certificateId) {

        try {
            return OBJECT_MAPPER.writeValueAsString(lakarutlatande());
        } catch (JsonProcessingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "{\"failed\":true}";
    }

    private Lakarutlatande lakarutlatande() {
        Lakarutlatande lakarutlatande = new Lakarutlatande();
        lakarutlatande.setId("123");

        Patient patient = new Patient();
        patient.setId("19001122-3344");
        lakarutlatande.setPatient(patient);

        Ovrigt ovrigt = new Ovrigt();
        ovrigt.setData("{\"resmal\":\"San Francisco\"}");
        lakarutlatande.setOvrigt(ovrigt);

        return lakarutlatande;
    }
}
