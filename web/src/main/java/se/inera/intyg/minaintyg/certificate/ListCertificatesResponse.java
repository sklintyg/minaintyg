package se.inera.intyg.minaintyg.certificate;

import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.certificate.service.dto.Certificate;

import java.util.List;

@Data
@Builder
public class ListCertificatesResponse {
    List<Certificate> content;
}
