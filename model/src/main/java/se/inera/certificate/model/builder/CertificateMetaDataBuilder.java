package se.inera.certificate.model.builder;

import org.joda.time.LocalDate;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;

/**
 * @author andreaskaltenbach
 */
public class CertificateMetaDataBuilder {

    private CertificateMetaData metaData;

    public CertificateMetaDataBuilder(String certificateId) {
        this(certificateId, "");
    }

    public CertificateMetaDataBuilder(String certificateId, String document) {
        Certificate certificate = new Certificate(certificateId, document);
        metaData = new CertificateMetaData(certificate);
    }

    public CertificateMetaDataBuilder certificateType(String certificateType) {
        metaData.setType(certificateType);
        return this;
    }

    public CertificateMetaDataBuilder civicRegistrationNumber(String civicRegistrationNumber) {
        metaData.setCivicRegistrationNumber(civicRegistrationNumber);
        return this;
    }

    public CertificateMetaDataBuilder validity(LocalDate fromDate, LocalDate toDate) {
        metaData.setValidFromDate(fromDate);
        metaData.setValidToDate(toDate);
        return this;
    }

    public CertificateMetaDataBuilder validity(String fromDate, String toDate) {
        return validity(new LocalDate(fromDate), new LocalDate(toDate));
    }

    public CertificateMetaDataBuilder careUnitName(String careUnitName) {
        metaData.setCareUnitName(careUnitName);
        return this;
    }

    public CertificateMetaDataBuilder signingDoctorName(String signingDoctorName) {
        metaData.setSigningDoctorName(signingDoctorName);
        return this;
    }

    public CertificateMetaDataBuilder signedDate(LocalDate signedDate) {
        metaData.setSignedDate(signedDate);
        return this;
    }

    public CertificateMetaDataBuilder deleted(boolean deleted) {
        metaData.setDeleted(deleted);
        return this;
    }

    public CertificateMetaData build() {
        return metaData;
    }
}
