package se.inera.intyg.minaintyg.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonService;
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.api.person.Status;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinaIntygUserDetailServiceImpl implements MinaIntygUserDetailService {

  private final GetPersonService getPersonService;
  private static final String SPACE = " ";
  private static final String EMPTY = "";

  @Override
  public MinaIntygUser buildPrincipal(String personId, LoginMethod loginMethod) {
    validatePersonId(personId);
    final var personResponse = getPersonService.getPerson(
        PersonRequest.builder()
            .personId(personId)
            .build()
    );
    if (!personResponse.getStatus().equals(Status.FOUND)) {
      handleCommunicationFault(personResponse.getStatus());
    }
    return getMinaIntygUser(personResponse, loginMethod);
  }

  private MinaIntygUser getMinaIntygUser(PersonResponse personResponse, LoginMethod loginMethod) {
    final var personId = personResponse.getPerson().getPersonnummer();
    final var personName = buildPersonName(personResponse.getPerson());
    return new MinaIntygUser(personId, personName, loginMethod);
  }

  private String buildPersonName(Person person) {
    return person.getFornamn()
        + SPACE
        + includeMiddleName(person.getMellannamn())
        + person.getEfternamn();
  }

  private String includeMiddleName(String middleName) {
    return middleName != null ? middleName + SPACE : EMPTY;
  }

  private static void handleCommunicationFault(Status status) {
    log.error("Error communicating with IntygProxyService, status from response: '{}",
        status);
    throw new RuntimeException("Error communication with IntygProxyService. Status: " + status);
  }

  private void validatePersonId(String personId) {
    if (personId == null || personId.trim().isEmpty()) {
      throw new IllegalArgumentException(
          String.format("personId must have a valid value: '%s'", personId));
    }
  }
}
