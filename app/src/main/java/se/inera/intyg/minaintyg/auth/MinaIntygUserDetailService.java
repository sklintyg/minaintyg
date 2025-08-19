package se.inera.intyg.minaintyg.auth;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.exception.UserInactiveException;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.logging.HashUtility;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinaIntygUserDetailService {


  @Value("${login.age.limit}")
  private long loginAgeLimit;

  private final HashUtility hashUtility;
  private final GetUserIntegrationService getUserIntegrationService;

  public MinaIntygUser buildPrincipal(String userId, LoginMethod loginMethod) {
    validateUserId(userId);
    final var userResponse = getUserIntegrationService.getUser(
        GetUserIntegrationRequest.builder()
            .userId(userId)
            .build()
    );

    if (!userResponse.getStatus().equals(Status.FOUND)) {
      handleCommunicationFault(userResponse.getStatus());
    }

    final var userIdFromResponse = userResponse.getUser().getUserId();

    if (!userResponse.getUser().isActive()) {
      handleInactiveUser(userIdFromResponse, loginMethod);
    }

    if (belowLoginAgeLimit(userIdFromResponse)) {
      handleUnderagePerson(userIdFromResponse, loginMethod);
    }

    return MinaIntygUser.builder()
        .userId(userIdFromResponse)
        .userName(userResponse.getUser().getName())
        .loginMethod(loginMethod)
        .build();
  }

  private void handleInactiveUser(String userId, LoginMethod loginMethod) {
    final var errorMessage = "Access denied for inactive user with id '%s'."
        .formatted(hashUtility.hash(userId));
    log.warn(errorMessage);
    throw new UserInactiveException(errorMessage, loginMethod);
  }

  private static void handleCommunicationFault(Status status) {
    log.error("Error communicating with IntygProxyService, status from response: '{}'", status);
    throw new IllegalStateException(
        "Error communication with IntygProxyService. Status: '%s' ".formatted(status)
    );
  }

  private void validateUserId(String userId) {
    if (userId == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "userId must have a valid value: '%s'".formatted(userId)
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

  private void handleUnderagePerson(String userId, LoginMethod loginMethod) {
    final var errorMessage = "Access denied for underage person with id '%s'."
        .formatted(hashUtility.hash(userId));
    log.warn(errorMessage);
    throw new LoginAgeLimitException(errorMessage, loginMethod);
  }
}
