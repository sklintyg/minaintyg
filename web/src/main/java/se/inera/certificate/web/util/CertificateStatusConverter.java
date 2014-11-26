package se.inera.certificate.web.util;

import java.util.List;

import se.inera.certificate.api.CertificateStatus;
import se.inera.certificate.api.StatusMeta;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeStatusType;
import se.inera.certificate.web.service.dto.UtlatandeStatusType.StatusType;

/**
 * Converts meta data from the internal {@link UtlatandeMetaData} to the REST service model {@link CertificateStatus}.
 */
public class CertificateStatusConverter {

    public static CertificateStatus toCertificateStatus(List<UtlatandeStatusType> statuses) {
        CertificateStatus result = new CertificateStatus();

        boolean cancelled = false;
        if (statuses != null) {
            for (UtlatandeStatusType statusType : statuses) {
                StatusMeta statusMeta = new StatusMeta();
                statusMeta.setType(statusType.getType().name());
                statusMeta.setTarget(statusType.getTarget());
                statusMeta.setTimestamp(statusType.getTimestamp());
                result.getStatuses().add(statusMeta);

                if (statusType.getType().equals(StatusType.CANCELLED)) {
                    cancelled = true;
                }
            }
        }
        result.setCancelled(cancelled);
        return result;
    }

}
