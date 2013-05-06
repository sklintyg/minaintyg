package se.inera.certificate.web.service;

import java.util.List;

import se.inera.certificate.api.CertificateMeta;

public interface CertificateService {
    /**
     * Retrives a list of certificates for the given civicRegistrationNumber
     * @param civicRegistrationNumber
     * @return
     */
    public List<CertificateMeta> getCertificates(String civicRegistrationNumber);
}
