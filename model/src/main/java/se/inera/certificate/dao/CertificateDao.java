package se.inera.certificate.dao;

import java.util.List;

import se.inera.certificate.model.CertificateMetaData;

public interface CertificateDao {

    List<CertificateMetaData> findMetaDataByCivicRegistrationNumberAndType(String civicRegistrationNumber, List<String> type);

    CertificateMetaData getCertificate(String certificateId);
    
    void store(CertificateMetaData certificateMetaData);
}
