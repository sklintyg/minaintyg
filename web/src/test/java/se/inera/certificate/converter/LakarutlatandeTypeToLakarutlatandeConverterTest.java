package se.inera.certificate.converter;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.integration.converter.LakarutlatandeTypeToLakarutlatandeConverter;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeTypeToLakarutlatandeConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException {

        // read LakarutlatandeType from file
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<LakarutlatandeType> lakarutlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("fk7263/maximalt-intyg.xml").getInputStream()), LakarutlatandeType.class);

        Lakarutlatande lakarutlatande = LakarutlatandeTypeToLakarutlatandeConverter.convert(lakarutlatandeElement.getValue());

        // serialize lakarutlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(lakarutlatande);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getInputStream());

        assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree, tree);
    }
}
