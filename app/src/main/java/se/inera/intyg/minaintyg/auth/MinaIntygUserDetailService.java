package se.inera.intyg.minaintyg.auth;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.exception.UserInactiveException;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.logging.HashUtility;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinaIntygUserDetailService {

  @Value("${login.age.limit}")
  private long loginAgeLimit;

  private final String CITIZEN_PROFILE = "citizen";

  private final HashUtility hashUtility;
  private final GetPersonIntegrationService getPersonIntegrationService;
  private final GetCitizenIntegrationService getCitizenIntegrationService;
  private final Environment environment;

  public MinaIntygUser buildPrincipal(String personId, LoginMethod loginMethod) {
    validatePersonId(personId);

    boolean useCitizenService = Arrays.stream(environment.getActiveProfiles())
        .anyMatch(CITIZEN_PROFILE::equalsIgnoreCase);

    if (useCitizenService) {
      final var citizenResponse = getCitizenIntegrationService.getCitizen(
          GetCitizenIntegrationRequest.builder()
              .citizenId(personId)
              .build()
      );

      final var personIdFromResponse = citizenResponse.getCitizen().getCitizenId();

      if (!citizenResponse.getCitizen().isActive()) {
        handleInactiveUser(personIdFromResponse, loginMethod);
      }

      if (belowLoginAgeLimit(personIdFromResponse)) {
        handleUnderagePerson(personIdFromResponse, loginMethod);
      }

      return MinaIntygUser.builder()
          .personId(personIdFromResponse)
          .personName(citizenResponse.getCitizen().getName())
          .loginMethod(loginMethod)
          .build();

    } else {

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
        handleUnderagePerson(personIdFromResponse, loginMethod);
      }

      return MinaIntygUser.builder()
          .personId(personIdFromResponse)
          .personName(personResponse.getPerson().getName())
          .loginMethod(loginMethod)
          .build();
    }
  }

  private void handleInactiveUser(String personId, LoginMethod loginMethod) {
    final var errorMessage = "Access denied for inactive user with id '%s'."
        .formatted(hashUtility.hash(personId));
    log.warn(errorMessage);
    throw new UserInactiveException(errorMessage, loginMethod);
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
    String birthDate = personId.substring(0, 8);
    final var dayOfMonth = Integer.parseInt(birthDate.substring(6, 8));

    if (isCoordinationNumber(dayOfMonth)) {
      birthDate = handleCoordinationNumber(birthDate, dayOfMonth);
    }

    final var date = LocalDate.parse(birthDate, DateTimeFormatter.BASIC_ISO_DATE);
    return date.plusYears(loginAgeLimit).isAfter(LocalDate.now(ZoneId.systemDefault()));
  }

  private boolean isCoordinationNumber(int dayOfMonth) {
    return dayOfMonth > 60;
  }

  private String handleCoordinationNumber(String birthDate, int dayOfMonth) {
    final var dayValue = String.valueOf(dayOfMonth - 60);
    return birthDate.substring(0, 6).concat(Strings.padStart(dayValue, 2, '0'));
  }

  private void handleUnderagePerson(String personId, LoginMethod loginMethod) {
    final var errorMessage = "Access denied for underage person with id '%s'."
        .formatted(hashUtility.hash(personId));
    log.warn(errorMessage);
    throw new LoginAgeLimitException(errorMessage, loginMethod);
  }
}
