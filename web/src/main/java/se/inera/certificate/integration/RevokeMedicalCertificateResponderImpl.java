package se.inera.certificate.integration;

import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;

@Transactional
@SchemaValidation
public class RevokeMedicalCertificateResponderImpl implements RevokeMedicalCertificateResponderInterface {

	@Autowired
	private CertificateService certificateService;
	
	@Override
	public RevokeMedicalCertificateResponseType revokeMedicalCertificate(AttributedURIType logicalAddress, RevokeMedicalCertificateRequestType parameters) {
		
		RevokeMedicalCertificateResponseType response = new RevokeMedicalCertificateResponseType();
		
		String certificateId = parameters.getRevoke().getLakarutlatande().getLakarutlatandeId();
		String civicRegistrationNumber = parameters.getRevoke().getLakarutlatande().getPatient().getPersonId().getExtension();
		
		Certificate certificate = certificateService.getCertificate(civicRegistrationNumber, certificateId);
		// TODO: Implementera revoke genom att skicka fraga med typ 'Makulera'
		
		
		response.setResult(ResultOfCallUtil.applicationErrorResult("Metoden är inte implementerad"));
		
		return response;
	}
}
