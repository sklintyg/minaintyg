package se.inera.intyg.minaintyg.integration.api.certificate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    String id;
    CertificateType type;
    CertificateSummary summary;
    CertificateIssuer issuer;
    CertificateUnit unit;
    List<CertificateRelation> relations;
    CertificateRecipient recipient;
    LocalDateTime issued;
}
