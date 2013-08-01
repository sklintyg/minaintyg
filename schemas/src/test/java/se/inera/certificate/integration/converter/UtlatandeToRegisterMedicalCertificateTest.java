package se.inera.certificate.integration.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * @author andreaskaltenbach
 */
public class UtlatandeToRegisterMedicalCertificateTest {

    @Test
    public void testConversion() throws JAXBException, IOException, SAXException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande utlatande = objectMapper.readValue(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getInputStream(), Utlatande.class);


        RegisterMedicalCertificateType registerMedicalCertificateType = UtlatandeToRegisterMedicalCertificate.getJaxbObject(utlatande);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificate
        String expectation = FileUtils.readFileToString(new ClassPathResource("register-medical-certificate/register-medical-certificate-maximalt-fk7263.xml").getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());

        Assert.assertTrue(diff.toString(), diff.identical());
    }

    private JAXBElement<?> wrapJaxb(RegisterMedicalCertificateType ws) {
            JAXBElement<?> jaxbElement = new JAXBElement<RegisterMedicalCertificateType>(
                    new QName("urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3", "RegisterMedicalCertificate"),
                    RegisterMedicalCertificateType.class, ws);
            return jaxbElement;
        }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        public int differenceFound(Difference difference) {
            if (DifferenceConstants.NAMESPACE_PREFIX_ID == difference.getId()) {
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
