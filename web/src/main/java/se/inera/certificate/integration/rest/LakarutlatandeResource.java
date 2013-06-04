package se.inera.certificate.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeResource implements IneraCertificateRestApi {

    @Autowired
    private CertificateService certificateService;
    
    @Override
    public String getCertificate(String certificateId) {
        Certificate certificate = certificateService.getCertificate(null, certificateId);
        if (certificate != null) {
            return certificate.getDocument();
        } else {
            return null;
        }
    }
}
