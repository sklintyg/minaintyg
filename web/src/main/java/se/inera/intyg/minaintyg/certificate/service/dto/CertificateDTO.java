package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;

import java.util.List;

@Data
@Builder
public class CertificateDTO {
    String id;
    CertificateType type;
    CertificateSummary summary;
    CertificateIssuer issuer;
    CertificateUnit unit;
    List<CertificateEvent> events;
    List<CertificateStatusType> statuses;
    String issued;
}
