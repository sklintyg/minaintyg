package se.inera.certificate.integration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

@RunWith(MockitoJUnitRunner.class)
public class RevokeMedicalCertificateResponderImplTest {

    private static final String CERTIFICATE_ID = "intygs-id-1234567890";
    private static final String PERSONNUMMER = "19121212-1212";

    private static final AttributedURIType ADDRESS = new AttributedURIType();

    @Mock
    private CertificateService certificateService;

    @Mock
    private SendMedicalCertificateQuestionResponderInterface sendMedicalCertificateQuestionResponderInterface;

    @InjectMocks
    private RevokeMedicalCertificateResponderInterface responder = new RevokeMedicalCertificateResponderImpl();

    private RevokeMedicalCertificateRequestType revokeRequest() throws Exception {
        // read request from file
        JAXBContext jaxbContext = JAXBContext.newInstance(RevokeMedicalCertificateRequestType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<RevokeMedicalCertificateRequestType> request = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("revoke-medical-certificate/revoke-medical-certificate-request.xml").getInputStream()), RevokeMedicalCertificateRequestType.class);
        return request.getValue();
    }

    private SendMedicalCertificateQuestionType expectedSendRequest() throws Exception {
        // read request from file
        JAXBContext jaxbContext = JAXBContext.newInstance(SendMedicalCertificateQuestionType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<SendMedicalCertificateQuestionType> request = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("revoke-medical-certificate/send-medical-certificate-question-request.xml").getInputStream()), SendMedicalCertificateQuestionType.class);
        return request.getValue();
    }

    @Test
    public void testRevokeCertificateWhichWasAlreadySentToForsakringskassan() throws Exception {

        Certificate certificate = new Certificate(CERTIFICATE_ID, "text");
        CertificateStateHistoryEntry historyEntry = new CertificateStateHistoryEntry("FK", CertificateState.SENT, new LocalDateTime());
        certificate.setStates(Collections.singletonList(historyEntry));

        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        SendMedicalCertificateQuestionResponseType sendQuestionResponse = new SendMedicalCertificateQuestionResponseType();
        sendQuestionResponse.setResult(okResult());
        when(sendMedicalCertificateQuestionResponderInterface.sendMedicalCertificateQuestion(ADDRESS, expectedSendRequest())).thenReturn(sendQuestionResponse);

        RevokeMedicalCertificateResponseType response = responder.revokeMedicalCertificate(ADDRESS, revokeRequest());

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(certificateService).setCertificateState(eq(PERSONNUMMER), eq(CERTIFICATE_ID), eq("FK"), eq(CertificateState.CANCELLED), any(LocalDateTime.class));
        verify(sendMedicalCertificateQuestionResponderInterface).sendMedicalCertificateQuestion(ADDRESS, expectedSendRequest());

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());
    }

    @Test
    public void testRevokeCertificateWithForsakringskassanReturningError() throws Exception {

        Certificate certificate = new Certificate(CERTIFICATE_ID, "text");
        CertificateStateHistoryEntry historyEntry = new CertificateStateHistoryEntry("FK", CertificateState.SENT, new LocalDateTime());
        certificate.setStates(Collections.singletonList(historyEntry));

        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        SendMedicalCertificateQuestionResponseType sendQuestionResponse = new SendMedicalCertificateQuestionResponseType();
        sendQuestionResponse.setResult(failResult("someErrorFromFörsäkringskassan"));
        when(sendMedicalCertificateQuestionResponderInterface.sendMedicalCertificateQuestion(ADDRESS, expectedSendRequest())).thenReturn(sendQuestionResponse);

        try {
            responder.revokeMedicalCertificate(ADDRESS, revokeRequest());
            fail();
        } catch (RuntimeException ex) {
            assertEquals("Informing Försäkringskassan about revoked certificate resulted in error", ex.getMessage());
        }
    }

    @Test
    public void testRevokeCertificateWhichWasNotSentToForsakringskassan() throws Exception {

        Certificate certificate = new Certificate(CERTIFICATE_ID, "text");

        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        RevokeMedicalCertificateResponseType response = responder.revokeMedicalCertificate(ADDRESS, revokeRequest());

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(certificateService).setCertificateState(eq(PERSONNUMMER), eq(CERTIFICATE_ID), eq("FK"), eq(CertificateState.CANCELLED), any(LocalDateTime.class));

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());
    }

    @Test
    public void testRevokeUnknownCertificate() throws Exception {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(null);

        RevokeMedicalCertificateResponseType response = responder.revokeMedicalCertificate(ADDRESS, revokeRequest());

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals("No certificate 'intygs-id-1234567890' found to revoke for patient '19121212-1212'.", response.getResult().getErrorText());
    }

    @Test
    public void testRevokeAlreadyRevokedCertificate() throws Exception {
        Certificate certificate = new Certificate(CERTIFICATE_ID, "text");
        CertificateStateHistoryEntry historyEntry = new CertificateStateHistoryEntry("FK", CertificateState.CANCELLED, new LocalDateTime());
        certificate.setStates(Collections.singletonList(historyEntry));

        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);

        RevokeMedicalCertificateResponseType response = responder.revokeMedicalCertificate(ADDRESS, revokeRequest());

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        assertEquals(ResultCodeEnum.INFO, response.getResult().getResultCode());
        assertEquals("Certificate 'intygs-id-1234567890' is already revoked.", response.getResult().getInfoText());
    }
}
