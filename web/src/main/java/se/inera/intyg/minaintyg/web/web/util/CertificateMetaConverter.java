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
package se.inera.intyg.minaintyg.web.web.util;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;

/**
 * Converts meta data from the internal {@link UtlatandeMetaData} to the REST service model {@link CertificateMeta}.
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

        boolean cancelled = false;
        if (meta.getStatuses() != null) {
            for (Status statusType : meta.getStatuses()) {
                result.getStatuses().add(statusType);

                if (statusType.getType().equals(CertificateState.CANCELLED)) {
                    cancelled = true;
                }
            }
        }
        result.setCancelled(cancelled);

        return result;
    }

    public static List<CertificateMeta> toCertificateMeta(List<UtlatandeMetaData> metas) {
        List<CertificateMeta> result = new ArrayList<>();

        for (UtlatandeMetaData certificateMetaType : metas) {
            result.add(toCertificateMeta(certificateMetaType));
        }

        return result;
    }
}
