package se.inera.certificate.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

import intyg.registreraintyg._1.RegistreraIntygResponderInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Holder;
import java.io.IOException;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderImplTest {

    @Mock
    private RegistreraIntygResponderInterface registreraIntygResponder = mock(RegistreraIntygResponderInterface.class);

    @InjectMocks
    private RegisterMedicalCertificateResponderInterface responder = new RegisterMedicalCertificateResponderImpl();

    @Test
    public void testRegisterCertificate() throws JAXBException, IOException {

        // read request from file
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<RegisterMedicalCertificateType> request =  unmarshaller.unmarshal(new StreamSource(new ClassPathResource("fk7263/fk7263.xml").getInputStream()), RegisterMedicalCertificateType.class);

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request.getValue());

        verify(registreraIntygResponder).registreraIntyg(any(Holder.class));


        assertEquals(OK, response.getResult().getResultCode());
    }
}
