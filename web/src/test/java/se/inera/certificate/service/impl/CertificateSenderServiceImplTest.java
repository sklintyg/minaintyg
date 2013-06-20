package se.inera.certificate.service.impl;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.integration.ResultOfCallUtil;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.builder.CertificateBuilder;
import se.inera.certificate.service.CertificateSenderService;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * @author andreaskaltenbach
 */
@RunWith( MockitoJUnitRunner.class )
public class CertificateSenderServiceImplTest {

    @Mock
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponder;

    @Mock
    private CertificateService certificateService;

    @InjectMocks
    private CertificateSenderService senderService = new CertificateSenderServiceImpl();

    @Test
    public void testSend() throws IOException, JAXBException {

        Certificate certificate = new CertificateBuilder("123456")
                .certificateType("fk7263")
                .build();
        Lakarutlatande lakarutlatande = new CustomObjectMapper().readValue(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getFile(), Lakarutlatande.class);
        when(certificateService.getLakarutlatande(certificate)).thenReturn(lakarutlatande);

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());
        when(registerMedicalCertificateResponder.registerMedicalCertificate(any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        senderService.sendCertificate(certificate, "fk");

        verify(certificateService).getLakarutlatande(certificate);
        verify(registerMedicalCertificateResponder).registerMedicalCertificate(any(AttributedURIType.class), any(RegisterMedicalCertificateType.class));

    }

    @Test(expected = ExternalWebServiceCallFailedException.class)
    public void testSendWithForsakringskassanDown() throws IOException {
        Certificate certificate = new CertificateBuilder("123456")
                .certificateType("fk7263")
                .build();
        Lakarutlatande lakarutlatande = new CustomObjectMapper().readValue(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getFile(), Lakarutlatande.class);
        when(certificateService.getLakarutlatande(certificate)).thenReturn(lakarutlatande);

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.failResult("error"));
        when(registerMedicalCertificateResponder.registerMedicalCertificate(any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        senderService.sendCertificate(certificate, "fk");
    }
}
