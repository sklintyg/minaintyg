package se.inera.certificate.integration.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import static org.custommonkey.xmlunit.DifferenceConstants.NAMESPACE_PREFIX_ID;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.inera.certificate.common.v1.Utlatande;

/**
 * @author andreaskaltenbach
 */
public class UtlatandeJaxbToUtlatandeToUtlatandeJaxbConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException, SAXException {

        File xmlFile = new ClassPathResource("generic/maximalt-fk7263.xml").getFile();

        // read utlatandeType from file
        JAXBContext jaxbContext = JAXBContext.newInstance(se.inera.certificate.common.v1.Utlatande.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<Utlatande> utlatandeElement = unmarshaller.unmarshal(new StreamSource(new FileInputStream(xmlFile)), Utlatande.class);

        se.inera.certificate.model.Utlatande utlatande = UtlatandeJaxbToUtlatandeConverter.convert(utlatandeElement.getValue());
        Utlatande result = new UtlatandeToUtlatandeJaxbConverter(utlatande).convert();

        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(result, stringWriter);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(FileUtils.readFileToString(xmlFile), stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());

        assertTrue(diff.toString(), diff.identical());
    }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        public int differenceFound(Difference difference) {
            if (NAMESPACE_PREFIX_ID == difference.getId()) {
                // differences in namespace prefix IDs are ok (eg. 'ns1' vs 'ns2'), as long as the namespace URI is the same
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            } else {
                return RETURN_ACCEPT_DIFFERENCE;
            }
        }

        public void skippedComparison(Node control, Node test) {
        }
    }
}
