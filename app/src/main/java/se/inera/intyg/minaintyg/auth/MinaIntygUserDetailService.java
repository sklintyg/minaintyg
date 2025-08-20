package se.inera.intyg.minaintyg.auth;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Supplier;
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
    validateUserId(personId);
    boolean useCitizenService = Arrays.stream(environment.getActiveProfiles())
        .anyMatch(CITIZEN_PROFILE::equalsIgnoreCase);
    if (useCitizenService) {
      final var citizenResponse = getCitizenIntegrationService.getCitizen(
          GetCitizenIntegrationRequest.builder()
              .citizenId(personId)
              .build()
      );
      return buildMinaIntygUserFromResponse(
          citizenResponse.getStatus(),
          () -> citizenResponse.getCitizen().getCitizenId(),
          () -> citizenResponse.getCitizen().getName(),
          () -> citizenResponse.getCitizen().isActive(),
          loginMethod
      );
    } else {
      final var personResponse = getPersonIntegrationService.getPerson(
          GetPersonIntegrationRequest.builder()
              .personId(personId)
              .build()
      );
      return buildMinaIntygUserFromResponse(
          personResponse.getStatus(),
          () -> personResponse.getPerson().getPersonId(),
          () -> personResponse.getPerson().getName(),
          () -> personResponse.getPerson().isActive(),
          loginMethod
      );
    }
  }

  private MinaIntygUser buildMinaIntygUserFromResponse(
      Object status,
      Supplier<String> idSupplier,
      Supplier<String> nameSupplier,
      Supplier<Boolean> isActiveSupplier,
      LoginMethod loginMethod
  ) {
    if (!status.equals(Status.FOUND)) {
      handleCommunicationFault(status);
    }
    final var userId = idSupplier.get();
    if (!isActiveSupplier.get()) {
      handleInactiveUser(userId, loginMethod);
    }
    if (belowLoginAgeLimit(userId)) {
      handleUnderageUser(userId, loginMethod);
    }
    return MinaIntygUser.builder()
        .personId(userId)
        .personName(nameSupplier.get())
        .loginMethod(loginMethod)
        .build();
  }

  private void handleInactiveUser(String userId, LoginMethod loginMethod) {
    final var errorMessage = "Access denied for inactive citizen with id '%s'."
        .formatted(hashUtility.hash(userId));
    log.warn(errorMessage);
    throw new UserInactiveException(errorMessage, loginMethod);
  }

  private static void handleCommunicationFault(Object status) {
    log.error("Error communicating with IntygProxyService, status from response: '{}'", status);
    throw new IllegalStateException(
        "Error communication with IntygProxyService. Status: '%s' ".formatted(status)
    );
  }

  private void validateUserId(String userId) {
    if (userId == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "personId must have a valid value: '%s'".formatted(userId)
      );
    }
  }

  private boolean belowLoginAgeLimit(String userId) {
    String birthDate = userId.substring(0, 8);
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

  private void handleUnderageUser(String userId, LoginMethod loginMethod) {
    final var errorMessage = "Access denied for underage person with id '%s'."
        .formatted(hashUtility.hash(userId));
    log.warn(errorMessage);
    throw new LoginAgeLimitException(errorMessage, loginMethod);
  }
}
