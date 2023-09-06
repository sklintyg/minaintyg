package se.inera.intyg.minaintyg.integration.api;

import se.inera.intyg.minaintyg.integration.dto.PersonRequest;
import se.inera.intyg.minaintyg.integration.dto.PersonResponse;

public interface PUIntegrationService {

    PersonResponse getPersonResponse(PersonRequest personRequest);
}
