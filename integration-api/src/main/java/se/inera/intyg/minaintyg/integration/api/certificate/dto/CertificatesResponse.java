package se.inera.intyg.minaintyg.integration.api.certificate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificatesResponse {
    private List<Certificate> content;
}
