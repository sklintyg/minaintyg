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
package se.inera.certificate.dao;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;

import java.util.List;

/**
 * Data Access Object for handling {@link Certificate}.
 *
 * @author parwenaker
 *
 */
public interface CertificateDao {

    /**
     * Retrieves a list of {@link Certificate} filtered by parameters.
     *
     * @param civicRegistrationNumber Civic registration number
     * @param types Type of certificate
     * @param fromDate From date when the certificate is valid
     * @param toDate To data when the certificate is valid
     * @return
     */
    List<Certificate> findCertificate(String civicRegistrationNumber, List<String> types, LocalDate fromDate, LocalDate toDate);

    /**
     * Gets one {@link Certificate}.
     *
     * @param certificateId Id of the Certificate
     *
     * @return {@link Certificate}
     */
    Certificate getCertificate(String certificateId);

    /**
     * Stores a {@link Certificate}.
     *
     * @param certificate
     */
    void store(Certificate certificate);

    void updateStatus(String id, String civicRegistrationNumber, CertificateState state, String target, LocalDateTime timestamp);
}
