package se.inera.certificate.service;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public interface CertificateService {

    List<CertificateMetaData> listCertificates(String civicRegistrationNumber, List<String> type);

    Certificate getCertificate(String civicRegistrationNumber, String certificateId);
}
