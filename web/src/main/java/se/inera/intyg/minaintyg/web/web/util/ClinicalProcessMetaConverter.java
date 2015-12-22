/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.minaintyg.web.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.riv.clinicalprocess.healthcond.certificate.v1.StatusType;
import se.riv.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;


/**
 * Converts meta data from {@link CertificateMetaType} to the internal {@link UtlatandeMetaData} type.
 */
public final class ClinicalProcessMetaConverter {

    private ClinicalProcessMetaConverter() {
    }

    private static final Comparator<? super CertificateMetaType> DESCENDING_DATE = new Comparator<CertificateMetaType>() {
        @Override
        public int compare(CertificateMetaType m1, CertificateMetaType m2) {
            return m2.getSignDate().compareTo(m1.getSignDate());
        }
    };

    public static UtlatandeMetaData toUtlatandeMetaData(CertificateMetaType meta) {
        UtlatandeMetaBuilder builder = new UtlatandeMetaBuilder();

        builder.id(meta.getCertificateId())
                .type(meta.getCertificateType())
                .issuerName(meta.getIssuerName())
                .facilityName(meta.getFacilityName())
                .signDate(meta.getSignDate())
                .available(meta.getAvailable())
                .additionalInfo(meta.getComplemantaryInfo());

        if (meta.getStatus() != null) {
            for (UtlatandeStatus statusType : meta.getStatus()) {
                if (statusType.getType() == StatusType.SENT || statusType.getType() == StatusType.CANCELLED) {
                    CertificateState internalStatusType = CertificateState.valueOf(statusType.getType().name());
                    builder.addStatus(internalStatusType, statusType.getTarget(), statusType.getTimestamp());
                }
            }
        }

        return builder.build();
    }

    public static List<UtlatandeMetaData> toUtlatandeMetaData(List<CertificateMetaType> metas) {
        List<UtlatandeMetaData> result = new ArrayList<>();

        // Copy and sort metadata
        List<CertificateMetaType> input = new ArrayList<>(metas);
        Collections.sort(input, DESCENDING_DATE);

        for (CertificateMetaType certificateMetaType : input) {
            result.add(toUtlatandeMetaData(certificateMetaType));
        }

        return result;
    }
}
