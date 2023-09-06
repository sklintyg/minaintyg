package se.inera.intyg.minaintyg.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.PUIntegrationService;
import se.inera.intyg.minaintyg.integration.dto.Person;
import se.inera.intyg.minaintyg.integration.dto.PersonRequest;
import se.inera.intyg.minaintyg.integration.dto.PersonResponse;
import se.inera.intyg.minaintyg.integration.dto.Status;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinaIntygUserDetailServiceImpl implements MinaIntygUserDetailService{

    private final PUIntegrationService puIntegrationService;
    private static final String SPACE = " ";
    @Override
    public Object getPrincipal(String personId) {
        validatePersonId(personId);
        final var personResponse = puIntegrationService.getPersonResponse(
            PersonRequest.builder()
                .personId(personId)
                .build()
        );
        if (!personResponse.getStatus().equals(Status.FOUND)) {
            handleCommunicationFault(personId, personResponse);
        }
        return getMinaIntygUser(personResponse);
    }

    private MinaIntygUser getMinaIntygUser(PersonResponse personResponse) {
        final var personId = personResponse.getPerson().getPersonnummer();
        final var personName = buildPersonName(personResponse.getPerson());
        return new MinaIntygUser(personId, personName);
    }

    private String buildPersonName(Person person) {
        return person.getFornamn() + SPACE + person.getEfternamn();
    }

    private static void handleCommunicationFault(String personId, PersonResponse personResponse) {
        if (personResponse.getStatus().equals(Status.NOT_FOUND)) {
            log.error("Person identified by '{}' not found in PU-service, cannot login.", personId);
            throw new PersonNotFoundException("Person identified by '" + personId + "' not found in PU-service");
        }
        if (personResponse.getStatus().equals(Status.ERROR)) {
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
