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

import org.joda.time.LocalDate;
import se.inera.certificate.integration.builder.CertificateMetaTypeBuilder;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.model.CertificateState;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private ModelConverter() {
    }

    public static CertificateMetaType toCertificateMetaType(CertificateMetaData source) {

        return new CertificateMetaTypeBuilder()
                .certificateId(source.getId())
                .certificateType(source.getType())
                .validity(new LocalDate(source.getValidFromDate()), new LocalDate(source.getValidToDate()))
                .issuerName(source.getSigningDoctorName())
                .facilityName(source.getCareUnitName())
                .signDate(new LocalDate(source.getSignedDate()))
                .available(source.getDeleted() ? "borttaget" : "ja") // TODO - Makulerat?
                .build();

        // TODO - convert certificate status
       /* if (source.getStatus() != null) {

                    List<CertificateState> list = meta.getStatus();
                    for (final CertificateState s : list) {
                        final CertificateStatusType type = new CertificateStatusType();
                        type.setTarget(s.getDestinator());
                        type.setTimestamp(toDateTime(s.getWhen()));

                        for (final StatusType t : StatusType.values()) {
                            if (t.name().equals(s.getType().name())) {
                                type.setType(t);
                                break;
                            }
                        }

                        metaType.getStatus().add(type);
                    }
                }  */
    }


    public static CertificateState toCertificateState(StatusType statusType) {

        switch (statusType) {
            case PROCESSED:
                return CertificateState.PROCESSED;
            case DELETED:
                return CertificateState.DELETED;
            case RESTORED:
                return CertificateState.RESTORED;
            case CANCELLED:
                return CertificateState.CANCELLED;
            case SENT:
                return CertificateState.SENT;
            case RECEIVED:
                return CertificateState.RECEIVED;
            case IN_PROGRESS:
                return CertificateState.IN_PROGRESS;
            default:
                return CertificateState.UNHANDLED;
        }
    }
}
