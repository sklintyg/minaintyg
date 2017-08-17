/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.util;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeMetaData;

/**
 * Converts meta data from the internal {@link UtlatandeMetaData} and {@link CertificateMetaData} to the REST service
 * model {@link CertificateMeta}.
 */
public final class CertificateMetaConverter {

    private CertificateMetaConverter() {
    }

    public static CertificateMeta toCertificateMeta(UtlatandeMetaData meta) {
        CertificateMeta result = new CertificateMeta();

        result.setId(meta.getId());
        result.setSelected(false);
        result.setType(meta.getType());
        result.setCaregiverName(meta.getIssuerName());
        result.setCareunitName(meta.getFacilityName());
        result.setSentDate(meta.getSignDate());
        result.setArchived(!Boolean.parseBoolean(meta.getAvailable()));
        result.setComplementaryInfo(meta.getComplemantaryInfo());
        result.getStatuses().addAll(meta.getStatuses());

        return result;
    }

    public static List<CertificateMeta> toCertificateMeta(List<UtlatandeMetaData> metas) {
        List<CertificateMeta> result = new ArrayList<>();

        for (UtlatandeMetaData certificateMetaType : metas) {
            result.add(toCertificateMeta(certificateMetaType));
        }

        return result;
    }

    /**
     * Converts CertificateMetaData types to Rest CertificateMeta, optionally filtering statuses to include only
     * selected statuses.
     *
     * @param metaData
     * @param statusFilter
     *            - List of CertificateState types to keep
     * @return
     */
    public static CertificateMeta toCertificateMeta(CertificateMetaData metaData, List<CertificateState> statusFilter) {
        CertificateMeta result = new CertificateMeta();

        result.setId(metaData.getCertificateId());
        result.setSelected(false);
        result.setType(metaData.getCertificateType());
        result.setCaregiverName(metaData.getIssuerName());
        result.setCareunitName(metaData.getFacilityName());
        result.setSentDate(metaData.getSignDate());
        result.setArchived(!metaData.isAvailable());
        result.setComplementaryInfo(metaData.getAdditionalInfo());

        for (Status status : metaData.getStatus()) {

            // Obey any status filter restrictions
            if (statusFilter == null || statusFilter.contains(status.getType())) {
                result.getStatuses().add(status);
            }

        }

        return result;
    }
}