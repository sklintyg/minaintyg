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
package se.inera.certificate.integration.converter;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private ModelConverter() { }

    public static CertificateMetaType toCertificateMetaType(CertificateMetaData source) {
        return new CertificateMetaType();
    }

    public static CertificateMetaType toCertificateMetaType(Certificate source) {
        return new CertificateMetaType();
    }
}
