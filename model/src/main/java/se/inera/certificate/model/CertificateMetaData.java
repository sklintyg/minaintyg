package se.inera.certificate.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
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
    private Date signedDate;

    /** Time from which this certificate is valid. */
    @Column(name = "VALID_FROM_DATE")
    private Date validFromDate;

    /** Time to which this certificate is valid. */
    @Column(name = "VALID_TO_DATE")
    private Date validToDate;

    /** If this certificate is deleted or not. */
    @Column(name = "DELETED", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean deleted = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID", insertable = false, updatable = false)
    private Certificate certificate;

    public CertificateMetaData(Certificate certificate) {
        this.certificate = certificate;
        this.id = certificate.getId();
    }

    public CertificateMetaData() {
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

    public Date getSignedDate() {
        return new Date(signedDate.getTime());
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = new Date(signedDate.getTime());
    }

    public Date getValidFromDate() {
        return new Date(validFromDate.getTime());
    }

    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = new Date(validFromDate.getTime());
    }

    public Date getValidToDate() {
        return new Date(validToDate.getTime());
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = new Date(validToDate.getTime());
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
