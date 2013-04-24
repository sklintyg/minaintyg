package se.inera.certificate.dao;

import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.model.CertificateMetaData;

/**
 * Data Access Object for handling {@link Certificate} and {@link CertificateMetaData}.
 *
 * @author parwenaker
 *
 */
public interface CertificateDao {

    /**
     * Retreives a list of {@link CertificateMetaData} filtered by parameters.
     *
     * @param civicRegistrationNumber Civic registration number
     * @param types Type of certificate
     * @param fromDate From date when the certificate is valid
     * @param toDate To data when the certificate is valid
     * @return
     */
    List<CertificateMetaData> findCertificateMetaData(String civicRegistrationNumber, List<String> types, LocalDate fromDate, LocalDate toDate);

    /**
     * Gets one {@link CertificateMetaData}.
     *
     * @param certificateId Id of the Certificate
     *
     * @return {@link CertificateMetaData}
     */
    CertificateMetaData getCertificate(String certificateId);

    /**
     * Stores a {@link CertificateMetaData}.
     *
     * @param certificateMetaData
     */
    void store(CertificateMetaData certificateMetaData);
}
