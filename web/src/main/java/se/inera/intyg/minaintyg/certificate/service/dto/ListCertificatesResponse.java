package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListCertificatesResponse {
    List<CertificateDTO> content;
}
