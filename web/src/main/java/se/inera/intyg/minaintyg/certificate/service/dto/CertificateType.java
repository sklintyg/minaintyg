package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificateType {
    String id;
    String name;
}
