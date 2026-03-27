/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.auth;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.exception.CitizenInactiveException;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.common.PersonIntegrationResponse;
import se.inera.intyg.minaintyg.logging.HashUtility;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinaIntygUserDetailService {

  @Value("${login.age.limit}")
  private long loginAgeLimit;

  private final HashUtility hashUtility;
  private final GetPersonIntegrationService getPersonIntegrationService;

  public MinaIntygUser buildPrincipal(String personId, LoginMethod loginMethod) {
    validatePersonId(personId);
    final var personIntegrationResponse =
        getPersonIntegrationService.getPerson(
            GetPersonIntegrationRequest.builder().personId(personId).build());

    if (!personIntegrationResponse.getStatus().equals(Status.FOUND)) {
      handleCommunicationFault(personIntegrationResponse.getStatus());
    }

    final var personIdFromResponse = personIntegrationResponse.getPerson().getPersonId();

    if (belowLoginAgeLimit(personIdFromResponse)) {
      handleUnderagePerson(personIdFromResponse, loginMethod);
    }

    if (inactivatedAccount(personIntegrationResponse)) {
      handleInactiveUser(personIdFromResponse, loginMethod);
    }

    return MinaIntygUser.builder()
        .personId(personIdFromResponse)
        .personName(personIntegrationResponse.getPerson().getName())
        .loginMethod(loginMethod)
        .build();
  }

  private static boolean inactivatedAccount(PersonIntegrationResponse personIntegrationResponse) {
    return Boolean.FALSE.equals(personIntegrationResponse.getPerson().getIsActive());
  }

  private void handleInactiveUser(String userId, LoginMethod loginMethod) {
    final var errorMessage =
        "Access denied for inactive citizen with id '%s'.".formatted(hashUtility.hash(userId));
    log.warn(errorMessage);
    throw new CitizenInactiveException(errorMessage, loginMethod);
  }

  private static void handleCommunicationFault(Status status) {
    log.error("Error communicating with IntygProxyService, status from response: '{}'", status);
    throw new IllegalStateException(
        "Error communication with IntygProxyService. Status: '%s' ".formatted(status));
  }

  private void validatePersonId(String personId) {
    if (personId == null || personId.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "personId must have a valid value: '%s'".formatted(personId));
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
    final var errorMessage =
        "Access denied for underage person with id '%s'.".formatted(hashUtility.hash(personId));
    log.warn(errorMessage);
    throw new LoginAgeLimitException(errorMessage, loginMethod);
  }
}
