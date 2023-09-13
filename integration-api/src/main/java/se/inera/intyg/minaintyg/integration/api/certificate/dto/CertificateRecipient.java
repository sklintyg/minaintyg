package se.inera.intyg.minaintyg.integration.api.certificate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRecipient {
    String id;
    String name;
    LocalDateTime sent;
}
