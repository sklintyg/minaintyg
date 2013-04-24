package se.inera.certificate.model;

import java.io.UnsupportedEncodingException;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * This class represents the document part of a certificate. The document is stored as a binary large object
 * in the database. The encoding is UTF-8.
 *
 * @author andreaskaltenbach
 */
@Entity
@Table(name = "CERTIFICATE")
public class Certificate {

    /** Id of the certificate. */
    @Id
    @Column(name = "ID")
    private String id;

    /** Certificate document. */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "DOCUMENT")
    private byte [] document;

    /**
     * Constructor that takes an id and a document.
     *
     * @param id the id
     * @param document the document
     */
    public Certificate(String id, String document) {
        this.id = id;
        this.document = toBytes(document);
    }

    /**
     * Constructor for JPA.
     */
    Certificate() {
        // Empty
    }

    /**
     * @return id
     */
    String getId() {
        return id;
    }

    /**
     * @return document
     */
    public String getDocument() {
        return fromBytes(this.document);
    }

    private byte [] toBytes(String data) {
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ModelException("Failed to convert String to bytes!", e);
        }
    }

    private String fromBytes(byte [] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ModelException("Failed to convert bytes to String!", e);
        }
    }
}
