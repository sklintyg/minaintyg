package se.inera.intyg.minaintyg.integration.api.certificate.model.common;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRecipient {

  private String id;
  private String name;
  private LocalDateTime sent;
}
