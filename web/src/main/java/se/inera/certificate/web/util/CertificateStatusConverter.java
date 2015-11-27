package se.inera.certificate.web.util;

import se.inera.certificate.api.CertificateStatus;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;

import java.util.List;

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
