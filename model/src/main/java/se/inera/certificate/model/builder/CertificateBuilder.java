package se.inera.certificate.model.builder;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;

/**
 * @author andreaskaltenbach
 */
public class CertificateBuilder {

    private Certificate certificate;

    public CertificateBuilder(String certificateId) {
        this(certificateId, "");
    }

    public CertificateBuilder(String certificateId, String document) {
        this.certificate = new Certificate(certificateId, document);
    }

    public CertificateBuilder certificateType(String certificateType) {
        certificate.setType(certificateType);
        return this;
    }

    public CertificateBuilder civicRegistrationNumber(String civicRegistrationNumber) {
        certificate.setCivicRegistrationNumber(civicRegistrationNumber);
        return this;
    }

    public CertificateBuilder validity(LocalDate fromDate, LocalDate toDate) {
        certificate.setValidFromDate(fromDate);
        certificate.setValidToDate(toDate);
        return this;
    }

    public CertificateBuilder validity(String fromDate, String toDate) {
        return validity(new LocalDate(fromDate), new LocalDate(toDate));
    }

    public CertificateBuilder careUnitName(String careUnitName) {
        certificate.setCareUnitName(careUnitName);
        return this;
    }

    public CertificateBuilder signingDoctorName(String signingDoctorName) {
        certificate.setSigningDoctorName(signingDoctorName);
        return this;
    }

    public CertificateBuilder signedDate(LocalDate signedDate) {
        certificate.setSignedDate(signedDate);
        return this;
    }

    public CertificateBuilder deleted(boolean deleted) {
        certificate.setDeleted(deleted);
        return this;
    }

    public CertificateBuilder state(CertificateState state, String target) {
        return state(state, target, null);
    }

    public CertificateBuilder state(CertificateState state, String target, LocalDateTime timestamp) {
        certificate.getStates().add(new CertificateStateHistoryEntry(target, state, timestamp));
        return this;
    }

    public Certificate build() {
        return certificate;
    }
}
