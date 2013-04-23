package se.inera.certificate.service;

import org.joda.time.LocalDate;
import se.inera.certificate.model.CertificateMetaData;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public interface CertificateService {

    List<CertificateMetaData> listCertificates(String civicRegistrationNumber, List<String> certificateTypes, LocalDate fromDate, LocalDate toDate);

    CertificateMetaData getCertificate(String civicRegistrationNumber, String certificateId);
}
