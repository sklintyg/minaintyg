package se.inera.certificate.integration.json;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Ovrigt;

/**
 * @author andreaskaltenbach
 */
public class OvrigtSerializerTest {

    @Test
    public void serializeEmptyOvrigt() throws JsonProcessingException {

        Lakarutlatande lakarutlatande = lakarutlatandeWithOvrigt(null);
        String json = new CustomObjectMapper().writeValueAsString(lakarutlatande);
        assertEquals("{\"id\":\"123456\"}", json);
    }

    @Test
    public void serializeOvrigtWithEmptyData() throws JsonProcessingException {
        Lakarutlatande lakarutlatande = lakarutlatandeWithOvrigt(new Ovrigt());
        String json = new CustomObjectMapper().writeValueAsString(lakarutlatande);
        assertEquals("{\"id\":\"123456\",\"ovrigt\":{}}", json);
    }

    @Test
    public void serializeOvrigtWithData() throws JsonProcessingException {
        Ovrigt ovrigt = new Ovrigt();
        ovrigt.setData("{\"somefield\":\"somevalue\"}");
        Lakarutlatande lakarutlatande = lakarutlatandeWithOvrigt(ovrigt);

        String json = new CustomObjectMapper().writeValueAsString(lakarutlatande);
        assertEquals("{\"id\":\"123456\",\"ovrigt\":{\"somefield\":\"somevalue\"}}", json);
    }

    private Lakarutlatande lakarutlatandeWithOvrigt(Ovrigt ovrigt) {

        Lakarutlatande lakarutlatande = new Lakarutlatande();
        lakarutlatande.setId("123456");

        lakarutlatande.setOvrigt(ovrigt);
        return lakarutlatande;
    }
}
