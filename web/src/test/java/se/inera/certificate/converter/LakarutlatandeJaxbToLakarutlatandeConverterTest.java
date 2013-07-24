package se.inera.certificate.converter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.converter.LakarutlatandeJaxbToLakarutlatandeConverter;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Utlatande;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeJaxbToLakarutlatandeConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException {

        // read LakarutlatandeType from file
        JAXBContext jaxbContext = JAXBContext.newInstance(se.inera.certificate.integration.v1.Lakarutlatande.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<se.inera.certificate.integration.v1.Lakarutlatande> lakarutlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("generic/maximalt-fk7263.xml").getInputStream()), se.inera.certificate.integration.v1.Lakarutlatande.class);

        Utlatande utlatande = LakarutlatandeJaxbToLakarutlatandeConverter.convert(lakarutlatandeElement.getValue());

        // serialize lakarutlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(utlatande);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getInputStream());

        assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree, tree);
    }
}
