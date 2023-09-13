package se.inera.intyg.minaintyg.integration.api.certificate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificatesRequest {
    String patientId;
    @Builder.Default
    List<String> years = Collections.emptyList();
    @Builder.Default
    List<String> units = Collections.emptyList();
    @Builder.Default
    List<String> certificateTypes = Collections.emptyList();
    @Builder.Default
    List<CertificateStatusType> statuses = Collections.emptyList();
}
