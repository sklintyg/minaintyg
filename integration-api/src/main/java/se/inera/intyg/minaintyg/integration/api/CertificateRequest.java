package se.inera.intyg.minaintyg.integration.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificateRequest {

    private boolean includeMessage;

}
