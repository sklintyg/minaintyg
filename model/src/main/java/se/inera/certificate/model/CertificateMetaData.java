/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.model;

import javax.persistence.*;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "TYPE", nullable = false)
    private String type;

    /** Name of the doctor that signed the certificate. */
    // TODO: naming? (PW)
    @Column(name = "SIGNING_DOCTOR_NAME", nullable = false)
    private String signingDoctorName;

    /** Name of care unit. */
    @Column(name = "CARE_UNIT_NAME", nullable = false)
    private String careUnitName;

    /** Civic registration number for patient. */
    @Column(name = "CIVIC_REGISTRATION_NUMBER", nullable = false)
    private String civicRegistrationNumber;

    /** Time this certificate was signed. */
    @Column(name = "SIGNED_DATE", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate signedDate;

    /** Time from which this certificate is valid. */
    @Column(name = "VALID_FROM_DATE", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate validFromDate;

    /** Time to which this certificate is valid. */
    @Column(name = "VALID_TO_DATE", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate validToDate;

    /** If this certificate is deleted or not. */
    @Column(name = "DELETED", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean deleted = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID", insertable = false, updatable = false)
    private Certificate certificate;

    @ElementCollection
      @CollectionTable(
            name="CERTIFICATE_STATE",
            joinColumns=@JoinColumn(name="CERTIFICATE_ID")
      )
    private List<CertificateStateHistoryEntry> states = new ArrayList<>();

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

    public List<CertificateStateHistoryEntry> getStates() {
        return states;
    }

    public void setStates(List<CertificateStateHistoryEntry> states) {
        this.states = states;
    }
}
