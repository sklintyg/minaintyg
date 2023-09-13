package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Certificate {
    String id;
    CertificateType type;
    CertificateSummary summary;
    CertificateIssuer issuer;
    CertificateUnit unit;
    List<CertificateEvent> events;
    List<CertificateStatusType> statuses;
    String issued;
}
