package se.inera.certificate.integration;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import iso.v21090.dt.v1.II;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;

import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

@RunWith(MockitoJUnitRunner.class)
public class RevokeMedicalCertificateResponderImplTest {

    @Mock
    private CertificateService certificateService;

    @Mock
    private SendMedicalCertificateQuestionResponderInterface sendMedicalCertificateQuestionResponderInterface;

    @InjectMocks
    private RevokeMedicalCertificateResponderInterface responder = new RevokeMedicalCertificateResponderImpl();

    @Test
    public void testRevoke() throws Exception {

        RevokeMedicalCertificateRequestType parameters = new RevokeMedicalCertificateRequestType();
        parameters.setRevoke(new RevokeType());
        parameters.getRevoke().setLakarutlatande(new LakarutlatandeEnkelType());
        parameters.getRevoke().getLakarutlatande().setLakarutlatandeId("intygs-id-1234567890");
        parameters.getRevoke().getLakarutlatande().setPatient(new PatientType());
        parameters.getRevoke().getLakarutlatande().getPatient().setPersonId(new II());
        parameters.getRevoke().getLakarutlatande().getPatient().getPersonId().setExtension("19121212-1212");

        Certificate certificate = new Certificate("intygs-id-1234567890", "text");

        when(certificateService.getCertificate("19121212-1212", "Intygs-id-1234567890")).thenReturn(certificate);

        AttributedURIType addr = new AttributedURIType();
        addr.setValue("EnEnhet");
        responder.revokeMedicalCertificate(addr, parameters);

        ArgumentCaptor<SendMedicalCertificateQuestionType> question = ArgumentCaptor.forClass(SendMedicalCertificateQuestionType.class);
        ArgumentCaptor<AttributedURIType> logicalAddress = ArgumentCaptor.forClass(AttributedURIType.class);

        verify(certificateService).setCertificateState(eq("19121212-1212"), eq("intygs-id-1234567890"), eq("FK"), eq(CertificateState.CANCELLED), any(LocalDateTime.class));
        verify(sendMedicalCertificateQuestionResponderInterface).sendMedicalCertificateQuestion(logicalAddress.capture(), question.capture());

        assertNotNull(logicalAddress.getValue());
        assertNotNull(question.getValue());

    }
}
