package se.inera.certificate.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author andreaskaltenbach
 */
@NamedQueries({
    @NamedQuery(name="CertificateMetaData.findByCivicRegistrationNumberAndType", 
                query="select c from CertificateMetaData c where civicRegistrationNumber=:civicRegistrationNumber and type in (:types)"),
    @NamedQuery(name="CertificateMetaData.findCertificateById",
                query="select c.certificate from CertificateMetaData c where id=:id")            
})
@Entity
@Table(name="CERTIFICATE_META_DATA")
public class CertificateMetaData {
	
	/** Identity of the certificate */
    @Id
	private String id;
	
	/** Type of the certificate */
	private String type;
	
	/** Name of the doctor that signed the certificate */
	// TODO: naming? (PW)
	private String signingDoctorName;
	
	/** Name of care unit */
	private String careUnitName;
	
	/** Civic registration number for patient */
	private String civicRegistrationNumber;
	
	/** Time this certificate was signed */
	private Date signedDate;
	
	/** Time from which this certificate is valid */
	private Date validFromDate;
	
	/** Time to which this certificate is valid */
	private Date validToDate;
	
	/** If this certificate is deleted or not */
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean deleted;
	
	/** The certificate in String representation */
	private String certificate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return signedDate;
	}

	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Date getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
}
