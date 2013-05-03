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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.joda.time.LocalDate;
import se.inera.certificate.integration.builder.CertificateMetaTypeBuilder;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

import java.util.HashMap;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private static final BiMap<CertificateState, StatusType> CERTIFICATE_STATE_MAP;

    static {
        CERTIFICATE_STATE_MAP = HashBiMap.create(new HashMap<CertificateState, StatusType>() {
            private static final long serialVersionUID = 1L;
        {
            put(CertificateState.PROCESSED, StatusType.PROCESSED);
            put(CertificateState.DELETED, StatusType.DELETED);
            put(CertificateState.RESTORED, StatusType.RESTORED);
            put(CertificateState.CANCELLED, StatusType.CANCELLED);
            put(CertificateState.SENT, StatusType.SENT);
            put(CertificateState.RECEIVED, StatusType.RECEIVED);
            put(CertificateState.IN_PROGRESS, StatusType.IN_PROGRESS);
            put(CertificateState.UNHANDLED, StatusType.UNHANDLED);
        } });
    }

    private ModelConverter() {
    }

    public static CertificateState toCertificateState(StatusType statusType) {
         return CERTIFICATE_STATE_MAP.inverse().get(statusType);
    }

    public static CertificateMetaType toCertificateMetaType(Certificate source) {

        CertificateMetaTypeBuilder builder = new CertificateMetaTypeBuilder()
                .certificateId(source.getId())
                .certificateType(source.getType())
                .validity(new LocalDate(source.getValidFromDate()), new LocalDate(source.getValidToDate()))
                .issuerName(source.getSigningDoctorName())
                .facilityName(source.getCareUnitName())
                .signDate(new LocalDate(source.getSignedDate()))
                .available(source.getDeleted() ? "borttaget" : "ja"); // TODO - Makulerat?

        for (CertificateStateHistoryEntry state : source.getStates()) {
            StatusType statusType = CERTIFICATE_STATE_MAP.get(state.getState());
            builder.status(statusType, state.getTarget(), state.getTimestamp());
        }

        return builder.build();
    }
}
