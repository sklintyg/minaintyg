package se.inera.certificate.model.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author andreaskaltenbach
 */
@Entity
@Table(name = "CONSENT")
public class Consent {

    @javax.persistence.Id
    @Column(name = "CIVIC_REGISTRATION_NUMBER")
    private String civicRegistrationNumber;

    public Consent() {
    }

    public Consent(String civicRegistrationNumber) {
        this.civicRegistrationNumber = civicRegistrationNumber;
    }

    public String getCivicRegistrationNumber() {
        return civicRegistrationNumber;
    }

    public void setCivicRegistrationNumber(String civicRegistrationNumber) {
        this.civicRegistrationNumber = civicRegistrationNumber;
    }
}
