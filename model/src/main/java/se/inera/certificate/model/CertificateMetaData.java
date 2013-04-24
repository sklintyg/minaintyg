package se.inera.certificate.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * This class represents the meta data around a certificate. The certificate document can be retrieved
 * from this class using the {@link CertificateMetaData#getDocument()} method.
 *
 * @author andreaskaltenbach
 */
@Entity
@Table(name = "CERTIFICATE_META_DATA")
public class CertificateMetaData {

    /** Identity of the certificate. */
    @Id
    @Column(name = "ID")
    private String id;

    /** Type of the certificate. */
    @Column(name = "TYPE")
    private String type;

    /** Name of the doctor that signed the certificate. */
    // TODO: naming? (PW)
    @Column(name = "SIGNING_DOCTOR_NAME")
    private String signingDoctorName;

    /** Name of care unit. */
    @Column(name = "CARE_UNIT_NAME")
    private String careUnitName;

    /** Civic registration number for patient. */
    @Column(name = "CIVIC_REGISTRATION_NUMBER")
    private String civicRegistrationNumber;

    /** Time this certificate was signed. */
    @Column(name = "SIGNED_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate signedDate;

    /** Time from which this certificate is valid. */
    @Column(name = "VALID_FROM_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate validFromDate;

    /** Time to which this certificate is valid. */
    @Column(name = "VALID_TO_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate validToDate;

    /** If this certificate is deleted or not. */
    @Column(name = "DELETED", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean deleted = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID", insertable = false, updatable = false)
    private Certificate certificate;

    /**
     * Constructor.
     *
     * @param certificate a {@link Certificate}
     */
    public CertificateMetaData(Certificate certificate) {
        this.certificate = certificate;
        this.id = certificate.getId();
    }

    /**
     * Constructor used by JPA.
     */
    CertificateMetaData() {
        // EMPTY
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSigningDoctorName() {
        return signingDoctorName;
    }

    public void setSigningDoctorName(String signingDoctorName) {
        this.signingDoctorName = signingDoctorName;
    }

    public String getCareUnitName() {
        return careUnitName;
    }

    public void setCareUnitName(String careUnitName) {
        this.careUnitName = careUnitName;
    }

    public String getCivicRegistrationNumber() {
        return civicRegistrationNumber;
    }

    public void setCivicRegistrationNumber(String civicRegistrationNumber) {
        this.civicRegistrationNumber = civicRegistrationNumber;
    }

    public LocalDate getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDate signedDate) {
        this.signedDate = signedDate;
    }

    public LocalDate getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(LocalDate validFromDate) {
        this.validFromDate = validFromDate;
    }

    public LocalDate getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(LocalDate validToDate) {
        this.validToDate = validToDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDocument() {
        return certificate.getDocument();
    }
}
