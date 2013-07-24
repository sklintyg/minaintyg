package se.inera.certificate.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.integration.converter.UtlatandeJaxbToUtlatandeConverter;
import se.inera.certificate.integration.json.CustomObjectMapper;

/**
 * @author andreaskaltenbach
 */
public class UtlatandeJaxbToUtlatandeConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException {

        // read utlatandeType from file
        JAXBContext jaxbContext = JAXBContext.newInstance(se.inera.certificate.common.v1.Utlatande.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<Utlatande> utlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("generic/maximalt-fk7263.xml").getInputStream()), Utlatande.class);

        se.inera.certificate.model.Utlatande utlatande = UtlatandeJaxbToUtlatandeConverter.convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(utlatande);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getInputStream());

        assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree, tree);
    }
}
