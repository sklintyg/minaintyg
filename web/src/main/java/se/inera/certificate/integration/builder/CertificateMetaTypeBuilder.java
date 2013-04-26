package se.inera.certificate.integration.builder;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

/**
 * @author andreaskaltenbach
 */
public class CertificateMetaTypeBuilder {

    private CertificateMetaType metaType;

    public CertificateMetaTypeBuilder() {
        metaType = new CertificateMetaType();
    }

    public CertificateMetaType build() {
        return metaType;
    }

    public CertificateMetaTypeBuilder certificateId(String certificateId) {
        metaType.setCertificateId(certificateId);
        return this;
    }

    public CertificateMetaTypeBuilder certificateType(String certificateType) {
        metaType.setCertificateType(certificateType);
        return this;
    }

    public CertificateMetaTypeBuilder validity(LocalDate fromDate, LocalDate toDate) {
        metaType.setValidFrom(fromDate);
        metaType.setValidTo(toDate);
        return this;
    }

    public CertificateMetaTypeBuilder issuerName(String issuerName) {
        metaType.setIssuerName(issuerName);
        return this;
    }

    public CertificateMetaTypeBuilder facilityName(String facilityName) {
        metaType.setFacilityName(facilityName);
        return this;
    }

    public CertificateMetaTypeBuilder signDate(LocalDate signDate) {
        metaType.setSignDate(signDate);
        return this;
    }

    public CertificateMetaTypeBuilder available(String available) {
        metaType.setAvailable(available);
        return this;
    }

    public CertificateMetaTypeBuilder status(StatusType status, String target, LocalDateTime timestamp) {
        CertificateStatusType certificateStatusType = new CertificateStatusType();
        certificateStatusType.setTarget(target);
        certificateStatusType.setTimestamp(timestamp);
        certificateStatusType.setType(status);
        metaType.getStatus().add(certificateStatusType);
        return this;
    }
}
