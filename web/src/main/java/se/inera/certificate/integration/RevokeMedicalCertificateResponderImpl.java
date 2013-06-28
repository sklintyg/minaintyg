package se.inera.certificate.integration;

import static se.inera.certificate.integration.ResultOfCallUtil.failResult;
import static se.inera.certificate.integration.ResultOfCallUtil.infoResult;
import static se.inera.certificate.integration.ResultOfCallUtil.okResult;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.ERROR;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.INFO;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

import org.apache.cxf.annotations.SchemaValidation;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import riv.insuranceprocess.healthreporting.medcertqa._1.Amnetyp;
import riv.insuranceprocess.healthreporting.medcertqa._1.InnehallType;
import riv.insuranceprocess.healthreporting.medcertqa._1.VardAdresseringsType;
import se.inera.certificate.exception.CertificateRevokedException;
import se.inera.certificate.exception.InvalidCertificateException;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestion.v1.rivtabp20.SendMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.QuestionToFkType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificatequestionresponder.v1.SendMedicalCertificateQuestionType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;

@Transactional
@SchemaValidation
public class RevokeMedicalCertificateResponderImpl implements RevokeMedicalCertificateResponderInterface {

    private static final Logger LOG = LoggerFactory.getLogger(RevokeMedicalCertificateResponderImpl.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private SendMedicalCertificateQuestionResponderInterface sendMedicalCertificateQuestionResponderInterface;

    @Override
    public RevokeMedicalCertificateResponseType revokeMedicalCertificate(AttributedURIType logicalAddress, RevokeMedicalCertificateRequestType request) {

        RevokeMedicalCertificateResponseType response = new RevokeMedicalCertificateResponseType();

        String certificateId = request.getRevoke().getLakarutlatande().getLakarutlatandeId();
        String civicRegistrationNumber = request.getRevoke().getLakarutlatande().getPatient().getPersonId().getExtension();

        try {
            Certificate certificate = certificateService.revokeCertificate(civicRegistrationNumber, certificateId);
            // if certificate was sent to Forsakringskassan before, we have to send a MAKULERING question to notify Forsakringskassan about the revocation
            if (certificate.wasSentToTarget("FK")) {
                String vardref = request.getRevoke().getVardReferensId();
                String meddelande = request.getRevoke().getMeddelande();

                // TODO: Vi behöver signeringstidpunkt för både fråga och intyg... /PW
                LocalDateTime signTs = request.getRevoke().getLakarutlatande().getSigneringsTidpunkt();
                LocalDateTime avsantTs = request.getRevoke().getAvsantTidpunkt();
                VardAdresseringsType vardAddress = request.getRevoke().getAdressVard();
                
                QuestionToFkType question = new QuestionToFkType();
                question.setAmne(Amnetyp.MAKULERING_AV_LAKARINTYG);
                question.setVardReferensId(vardref);
                question.setAvsantTidpunkt(avsantTs);
                question.setAdressVard(vardAddress);

                question.setFraga(new InnehallType());
                question.getFraga().setMeddelandeText(meddelande);
                question.getFraga().setSigneringsTidpunkt(signTs);

                question.setLakarutlatande(request.getRevoke().getLakarutlatande());

                SendMedicalCertificateQuestionType parameters = new SendMedicalCertificateQuestionType();
                parameters.setQuestion(question);

                SendMedicalCertificateQuestionResponseType sendResponse = sendMedicalCertificateQuestionResponderInterface.sendMedicalCertificateQuestion(logicalAddress, parameters);
                if (sendResponse.getResult().getResultCode() != OK) {
                    handleForsakringskassaError(certificateId, sendResponse.getResult());
                }
            }
        } catch (InvalidCertificateException e) {
            // return with ERROR response if certificate was not found
            LOG.info("Tried to revoke certificate '" + certificateId + "' for patient '" + civicRegistrationNumber + "' but certificate does not exist");
            response.setResult(failResult("No certificate '" + certificateId + "' found to revoke for patient '" + civicRegistrationNumber + "'."));
            return response;
        } catch (CertificateRevokedException e) {
            // return with INFO response if certificate was revoked before
            LOG.info("Tried to revoke certificate '" + certificateId + "' for patient '" + civicRegistrationNumber + "' which already is revoked");
            response.setResult(infoResult("Certificate '" + certificateId + "' is already revoked."));
            return response;
        }

        response.setResult(okResult());
        return response;
    }

    private void handleForsakringskassaError(String certificateId, ResultOfCall result) {
        if (result.getResultCode() == INFO) {
            LOG.error("Failed to send question to Försäkringskassan for revoking certificate '" + certificateId + "'. Info from forsakringskassan: " + result.getInfoText());
        }
        if (result.getResultCode() == ERROR) {
            LOG.error("Failed to send question to Försäkringskassan for revoking certificate '" + certificateId + "'. Error from forsakringskassan: " + result.getErrorId() + " - " + result.getErrorText());
        }
        throw new RuntimeException("Informing Försäkringskassan about revoked certificate resulted in error");
    }
}
