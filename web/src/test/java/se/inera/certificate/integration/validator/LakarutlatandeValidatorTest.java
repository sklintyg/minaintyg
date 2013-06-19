package se.inera.certificate.integration.validator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
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
public class LakarutlatandeValidatorTest {

    private static Unmarshaller UNMARSHALLER;

    @BeforeClass
    public static void setupJaxb() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        UNMARSHALLER = jaxbContext.createUnmarshaller();
    }

    private LakarutlatandeType lakarutlatande() throws IOException, JAXBException {
        // read request from file
        JAXBElement<RegisterMedicalCertificateType> request = UNMARSHALLER.unmarshal(new StreamSource(new ClassPathResource("register-medical-certificate/register-medical-certificate-valid.xml").getInputStream()), RegisterMedicalCertificateType.class);
        return request.getValue().getLakarutlatande();
    }

    @Test
    public void testHappyCase() throws Exception {
        new LakarutlatandeValidator(lakarutlatande()).validate();
    }

    @Test(expected = ValidationException.class)
    public void testWrongPatientIdCodeSystem() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getPatient().getPersonId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }


    @Test(expected = ValidationException.class)
    public void testWrongSkapadAvHosPersonalCodeSystem() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getPersonalId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test(expected = ValidationException.class)
    public void testMissingEnhetRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getEnhetsId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test(expected = ValidationException.class)
    public void testMissingVardgivareRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test(expected = ValidationException.class)
    public void testMissingMedicinsktTillstandCodeSystemName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getMedicinsktTillstand().getTillstandskod().setCodeSystemName(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test(expected = ValidationException.class)
    public void testIllegalMedicinsktTillstandCodeSystemName() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getMedicinsktTillstand().getTillstandskod().setCodeSystemName("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }


    @Test(expected = ValidationException.class)
    public void testMissingArbetsplatskodRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setRoot(null);

        new LakarutlatandeValidator(lakarutlatande).validate();
    }

    @Test( expected = ValidationException.class )
    public void testWrongArbetsplatskodRoot() throws Exception {
        LakarutlatandeType lakarutlatande = lakarutlatande();
        lakarutlatande.getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setRoot("<strange>");

        new LakarutlatandeValidator(lakarutlatande).validate();
    }
}
