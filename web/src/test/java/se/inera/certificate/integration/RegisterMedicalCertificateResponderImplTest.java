package se.inera.certificate.integration;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private RegisterMedicalCertificateResponderInterface responder = new RegisterMedicalCertificateResponderImpl();

    @Test
    public void testRegisterCertificate() throws JAXBException, IOException {

        // read request from file
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<RegisterMedicalCertificateType> request =  unmarshaller.unmarshal(new StreamSource(new ClassPathResource("fk7263/fk7263.xml").getInputStream()), RegisterMedicalCertificateType.class);

        ArgumentCaptor<Certificate> argument = ArgumentCaptor.forClass(Certificate.class);

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request.getValue());

        verify(certificateService).storeCertificate(argument.capture());

        assertEquals("6ea04fd0-5fef-4809-823b-efeddf8a4d55", argument.getValue().getId());
        assertEquals("Landstinget Norrland", argument.getValue().getCareUnitName());
        assertEquals("19940701-0066", argument.getValue().getCivicRegistrationNumber());
        assertEquals(new LocalDate("2013-03-17"), argument.getValue().getSignedDate());
        assertEquals(new LocalDate("2013-03-17"), argument.getValue().getValidFromDate());
        assertEquals(new LocalDate("2013-05-01"), argument.getValue().getValidToDate());

        assertEquals(OK, response.getResult().getResultCode());
    }
}
