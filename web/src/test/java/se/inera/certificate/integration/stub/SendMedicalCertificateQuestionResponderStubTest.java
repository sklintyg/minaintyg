package se.inera.certificate.integration.stub;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;

import riv.insuranceprocess.healthreporting.medcertqa._1.Amnetyp;
import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.QuestionToFkType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType;

@RunWith(MockitoJUnitRunner.class)
public class SendMedicalCertificateQuestionResponderStubTest {

    @Mock
    FkMedicalCertificatesStore store;
    
    @InjectMocks
    SendMedicalCertificateQuestionResponderStub stub = new SendMedicalCertificateQuestionResponderStub();
    
    @Test
    public void test() throws Exception {
        AttributedURIType logicalAddress = new AttributedURIType();
        SendMedicalCertificateQuestionType request = new SendMedicalCertificateQuestionType();
        QuestionToFkType question = new QuestionToFkType();
        question.setAmne(Amnetyp.MAKULERING_AV_LAKARINTYG);
        LakarutlatandeEnkelType lakarutlatande = new LakarutlatandeEnkelType();
        lakarutlatande.setLakarutlatandeId("id-1234567890");
        question.setLakarutlatande(lakarutlatande );
        request.setQuestion(question );
        
        stub.sendMedicalCertificateQuestion(logicalAddress, request);
        
        verify(store).makulera("id-1234567890");
    }
}
