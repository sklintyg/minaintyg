package se.inera.certificate.integration;

import iso.v21090.dt.v1.II;

import org.apache.cxf.annotations.SchemaValidation;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import riv.insuranceprocess.healthreporting.medcertqa._1.Amnetyp;
import riv.insuranceprocess.healthreporting.medcertqa._1.InnehallType;
import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import riv.insuranceprocess.healthreporting.medcertqa._1.VardAdresseringsType;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.QuestionToFkType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

@Transactional
@SchemaValidation
public class RevokeMedicalCertificateResponderImpl implements RevokeMedicalCertificateResponderInterface {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private SendMedicalCertificateQuestionResponderInterface sendMedicalCertificateQuestionResponderInterface;

    @Override
    public RevokeMedicalCertificateResponseType revokeMedicalCertificate(AttributedURIType logicalAddress, RevokeMedicalCertificateRequestType request) {

        RevokeMedicalCertificateResponseType response = new RevokeMedicalCertificateResponseType();

        String certificateId = request.getRevoke().getLakarutlatande().getLakarutlatandeId();
        String civicRegistrationNumber = request.getRevoke().getLakarutlatande().getPatient().getPersonId().getExtension();
        String vardref = request.getRevoke().getVardReferensId();
        String meddelande = request.getRevoke().getMeddelande();
        
        // TODO: Vi behöver signeringstidpunkt för både fråga och intyg... /PW
        LocalDateTime signTs = request.getRevoke().getLakarutlatande().getSigneringsTidpunkt();
        LocalDateTime avsantTs = request.getRevoke().getAvsantTidpunkt();
        VardAdresseringsType vardAddress = request.getRevoke().getAdressVard();

        certificateService.setCertificateState(civicRegistrationNumber, certificateId, "FK", CertificateState.CANCELLED, new LocalDateTime());

        SendMedicalCertificateQuestionType parameters = new SendMedicalCertificateQuestionType();
        QuestionToFkType question = new QuestionToFkType();
        question.setAmne(Amnetyp.MAKULERING_AV_LAKARINTYG);
        question.setVardReferensId(vardref);
        question.setAvsantTidpunkt(avsantTs);
        question.setAdressVard(vardAddress);

        question.setFraga(new InnehallType());
        question.getFraga().setMeddelandeText(meddelande);
        question.getFraga().setSigneringsTidpunkt(signTs);

        question.setLakarutlatande(getLakarutlatande(certificateId, civicRegistrationNumber, signTs));
        parameters.setQuestion(question);

        sendMedicalCertificateQuestionResponderInterface.sendMedicalCertificateQuestion(logicalAddress, parameters);

        response.setResult(ResultOfCallUtil.okResult());

        return response;
    }

    private LakarutlatandeEnkelType getLakarutlatande(String certificateId, String civicRegistrationNumber, LocalDateTime signTs) {
        LakarutlatandeEnkelType lakarutlatande = new LakarutlatandeEnkelType();
        lakarutlatande.setLakarutlatandeId(certificateId);
        lakarutlatande.setSigneringsTidpunkt(signTs);
        lakarutlatande.setPatient(new PatientType());
        lakarutlatande.getPatient().setPersonId(new II());
        lakarutlatande.getPatient().getPersonId().setExtension(civicRegistrationNumber);
        return lakarutlatande;
    }
}
