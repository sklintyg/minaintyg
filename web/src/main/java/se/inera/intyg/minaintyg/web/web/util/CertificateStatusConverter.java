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

import java.util.List;

import se.inera.intyg.minaintyg.web.api.CertificateStatus;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;

/**
 * Converts meta data to the REST service model {@link CertificateStatus}.
 */
public final class CertificateStatusConverter {

    private CertificateStatusConverter() {
    }

    public static CertificateStatus toCertificateStatus(List<Status> statuses) {
        CertificateStatus result = new CertificateStatus();

        boolean cancelled = false;
        if (statuses != null) {
            for (Status statusType : statuses) {
                result.getStatuses().add(statusType);

                if (statusType.getType().equals(CertificateState.CANCELLED)) {
                    cancelled = true;
                }
            }
        }
        result.setCancelled(cancelled);
        return result;
    }

}
