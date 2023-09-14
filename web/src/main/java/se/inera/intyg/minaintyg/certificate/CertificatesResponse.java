package se.inera.intyg.minaintyg.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.certificate.service.dto.CertificateDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificatesResponse {
    List<CertificateDTO> content;
}
