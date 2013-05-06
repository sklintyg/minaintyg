package se.inera.certificate.integration;

import static se.inera.certificate.integration.ResultOfCallUtil.okResult;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType;

@Transactional
@SchemaValidation
public class SendMedicalCertificateResponderImpl implements SendMedicalCertificateResponderInterface {
	
	@Autowired
	private CertificateService certificateService;
	
	@Override
	public SendMedicalCertificateResponseType sendMedicalCertificate(AttributedURIType logicalAddress, SendMedicalCertificateRequestType parameters) {
		SendMedicalCertificateResponseType response = new SendMedicalCertificateResponseType();
		
		String certificateId = parameters.getSend().getLakarutlatande().getLakarutlatandeId();
		String civicRegistrationNumber = parameters.getSend().getLakarutlatande().getPatient().getPersonId().getExtension();
		
		Certificate certificate = certificateService.getCertificate(civicRegistrationNumber, certificateId);
		
		String certificateDocument = certificate.getDocument();
		
		// TODO: Implementera skicka intyg till FK.
		
		response.setResult(ResultOfCallUtil.applicationErrorResult("Metoden är inte implementerad"));

		return response;
	}

}
