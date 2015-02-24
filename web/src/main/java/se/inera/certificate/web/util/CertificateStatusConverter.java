package se.inera.certificate.web.util;

import java.util.List;

import se.inera.certificate.api.CertificateStatus;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;

/**
 * Converts meta data from the internal {@link UtlatandeMetaData} to the REST service model {@link CertificateStatus}.
 */
public class CertificateStatusConverter {

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
