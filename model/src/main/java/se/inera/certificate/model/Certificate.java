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
