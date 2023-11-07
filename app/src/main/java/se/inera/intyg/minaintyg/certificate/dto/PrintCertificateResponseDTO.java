package se.inera.intyg.minaintyg.certificate.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PrintCertificateResponseDTO {

  String filename;
  byte[] pdfData;
}
