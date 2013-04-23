package se.inera.certificate.dao;

import org.joda.time.LocalDate;
import se.inera.certificate.model.CertificateMetaData;

import java.util.List;

public interface CertificateDao {

    List<CertificateMetaData> findCertificateMetaData(String civicRegistrationNumber, List<String> types, LocalDate fromDate, LocalDate toDate);

    CertificateMetaData getCertificate(String certificateId);

    void store(CertificateMetaData certificateMetaData);
}
