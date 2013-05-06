package se.inera.certificate.model;

import javax.persistence.*;

/**
 * @author andreaskaltenbach
 */
@Entity
@Table(name = "CONSENT")
public class Consent {

    @javax.persistence.Id
    @Column( name = "CIVIC_REGISTRATION_NUMBER" )
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
