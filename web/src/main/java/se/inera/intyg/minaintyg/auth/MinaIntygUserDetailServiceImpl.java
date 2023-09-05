package se.inera.intyg.minaintyg.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.PUIntegrationService;
import se.inera.intyg.minaintyg.integration.dto.PUResponse;
import se.inera.intyg.minaintyg.integration.dto.Status;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinaIntygUserDetailServiceImpl implements MinaIntygUserDetailService{

    private final PUIntegrationService puIntegrationService;
    @Override
    public Object getPrincipal(String personId) {
        validatePersonId(personId);
        final var puResponse = puIntegrationService.get(personId);
        validatePUResponse(personId, puResponse);
        return new MinaIntygUser(puResponse.getPerson().getPersonId(), puResponse.getPerson().getName());
    }

    private static void validatePUResponse(String personId, PUResponse puResponse) {
        if (puResponse.getStatus().equals(Status.NOT_FOUND)) {
            log.error("Person identified by '{}' not found in PU-service, cannot login.", personId);
            throw new PersonNotFoundException("Person identified by '" + personId + "' not found in PU-service");
        }
        if (puResponse.getStatus().equals(Status.ERROR)) {
            log.warn("Error communicating with PU service, cannot query person '{}", personId);
            throw new PUServiceException("Error communication with PU-service");
        }
    }

    private void validatePersonId(String personId) {
        if (personId == null || personId.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format("personId must have a valid value: '%s'", personId));
        }
    }
}
