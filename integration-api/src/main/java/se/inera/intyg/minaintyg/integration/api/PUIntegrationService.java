package se.inera.intyg.minaintyg.integration.api;

import se.inera.intyg.minaintyg.integration.dto.PUResponse;

public interface PUIntegrationService {

    PUResponse get(String personId);
}
