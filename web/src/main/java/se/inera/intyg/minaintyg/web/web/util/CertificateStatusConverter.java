package se.inera.certificate.web.util;

import java.util.List;

import se.inera.certificate.api.CertificateStatus;
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
