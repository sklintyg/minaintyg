package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintCertificateResponseDTO {

  private String filename;
  private byte[] pdfData;
}
