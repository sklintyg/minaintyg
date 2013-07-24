package se.inera.certificate.integration.json;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Utlatande;

/**
 * @author andreaskaltenbach
 */
public class OvrigtSerializerTest {

    @Test
    public void serializeEmptyOvrigt() throws JsonProcessingException {

        Utlatande utlatande = lakarutlatandeWithOvrigt(null);
        String json = new CustomObjectMapper().writeValueAsString(utlatande);
        assertEquals("{\"id\":{\"extension\":\"123456\"}}", json);
    }

    @Test
    public void serializeOvrigtWithEmptyData() throws JsonProcessingException {
        Utlatande utlatande = lakarutlatandeWithOvrigt(new Ovrigt());
        String json = new CustomObjectMapper().writeValueAsString(utlatande);
        assertEquals("{\"id\":{\"extension\":\"123456\"},\"ovrigt\":{}}", json);
    }

    @Test
    public void serializeOvrigtWithData() throws JsonProcessingException {
        Ovrigt ovrigt = new Ovrigt();
        ovrigt.setData("{\"somefield\":\"somevalue\"}");
        Utlatande utlatande = lakarutlatandeWithOvrigt(ovrigt);

        String json = new CustomObjectMapper().writeValueAsString(utlatande);
        assertEquals("{\"id\":{\"extension\":\"123456\"},\"ovrigt\":{\"somefield\":\"somevalue\"}}", json);
    }

    private Utlatande lakarutlatandeWithOvrigt(Ovrigt ovrigt) {

        Utlatande utlatande = new Utlatande();
        utlatande.setId(new Id("123456"));

        utlatande.setOvrigt(ovrigt);
        return utlatande;
    }
}
