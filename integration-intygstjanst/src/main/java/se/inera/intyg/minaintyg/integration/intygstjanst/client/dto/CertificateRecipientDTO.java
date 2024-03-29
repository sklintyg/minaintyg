package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRecipientDTO {

  private String id;
  private String name;
  private LocalDateTime sent;
}
