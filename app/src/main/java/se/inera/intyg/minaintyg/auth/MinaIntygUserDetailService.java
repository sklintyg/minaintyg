package se.inera.intyg.minaintyg.auth;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.util.HashUtility;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinaIntygUserDetailService {


  @Value("${login.age.limit}")
  private long loginAgeLimit;

  private final GetPersonIntegrationService getPersonIntegrationService;

  public MinaIntygUser buildPrincipal(String personId, LoginMethod loginMethod) {
    validatePersonId(personId);
    final var personResponse = getPersonIntegrationService.getPerson(
        GetPersonIntegrationRequest.builder()
            .personId(personId)
            .build()
    );

    if (!personResponse.getStatus().equals(Status.FOUND)) {
      handleCommunicationFault(personResponse.getStatus());
    }

    final var personIdFromResponse = personResponse.getPerson().getPersonId();

    if (belowLoginAgeLimit(personIdFromResponse)) {
      handleUnderagePerson(personIdFromResponse);
    }

    return MinaIntygUser.builder()
        .personId(personIdFromResponse)
        .personName(personResponse.getPerson().getName())
        .loginMethod(loginMethod)
        .build();
  }

  private static void handleCommunicationFault(Status status) {
    log.error("Error communicating with IntygProxyService, status from response: '{}'", status);
    throw new IllegalStateException(
        "Error communication with IntygProxyService. Status: '%s' ".formatted(status)
    );
  }

  private void validatePersonId(String personId) {
    if (personId == null || personId.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "personId must have a valid value: '%s'".formatted(personId)
      );
    }
  }

  private boolean belowLoginAgeLimit(String personId) {
    final var personBirthDate = LocalDate.parse(personId.substring(0, 8),
        DateTimeFormatter.BASIC_ISO_DATE);
    return personBirthDate.plusYears(loginAgeLimit).isAfter(LocalDate.now(ZoneId.systemDefault()));
  }

  private void handleUnderagePerson(String personId) {
    final var errorMessage = "Access denied for underage person with id '%s'."
        .formatted(HashUtility.hash(personId));
    log.warn(errorMessage);
    throw new LoginAgeLimitException(errorMessage);
  }
}
