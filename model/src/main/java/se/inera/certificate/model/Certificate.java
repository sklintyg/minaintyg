package se.inera.certificate.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * @author andreaskaltenbach
 */
@Entity
@Table(name="CERTIFICATE")
public class Certificate {
    
    /** Id of the certificate */
    @Id
    @Column(name="ID")
    private String id;
    
    /** Certificate document */
    @Lob
    @Basic(fetch=FetchType.EAGER)
    @Column(name="DOCUMENT")
    private byte [] document;
    
}
