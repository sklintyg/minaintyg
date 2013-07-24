package se.inera.certificate.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
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
import se.inera.certificate.integration.converter.LakarutlatandeTypeToUtlatandeConverter;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeTypeToUtlatandeConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException, SAXException {

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, Utlatande.class);

        // read LakarutlatandeType from file
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<LakarutlatandeType> lakarutlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("fk7263/maximalt-intyg.xml").getInputStream()), LakarutlatandeType.class);

        Utlatande utlatande = LakarutlatandeTypeToUtlatandeConverter.convert(lakarutlatandeElement.getValue());

        // read expected XML and compare with resulting lakarutlatande
        String expectation = FileUtils.readFileToString(new ClassPathResource("generic/maximalt-fk7263.xml").getFile());

        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(utlatande, stringWriter);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
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
