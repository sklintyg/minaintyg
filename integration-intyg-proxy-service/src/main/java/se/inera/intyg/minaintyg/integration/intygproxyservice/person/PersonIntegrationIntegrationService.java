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
package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import static se.inera.intyg.minaintyg.integration.api.citizen.CitizenConstants.CITIZEN_IPS_INTEGRATION;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationService;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyService;

@Slf4j
@Service
@Profile("!" + CITIZEN_IPS_INTEGRATION)
@RequiredArgsConstructor
public class PersonIntegrationIntegrationService implements GetPersonIntegrationService {

  private final GetPersonFromIntygProxyService getPersonFromIntygProxyService;
  private final PersonSvarConverter personSvarConverter;

  @Override
  public GetPersonIntegrationResponse getPerson(GetPersonIntegrationRequest personRequest) {
    validateRequest(personRequest);
    final var personSvarDTO = getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest);
    return GetPersonIntegrationResponse.builder()
        .person(personSvarConverter.convertPerson(personSvarDTO.getPerson()))
        .status(personSvarConverter.convertStatus(personSvarDTO.getStatus()))
        .build();
  }

  private void validateRequest(GetPersonIntegrationRequest personRequest) {
    if (personRequest == null
        || personRequest.getPersonId() == null
        || personRequest.getPersonId().isEmpty()) {
      throw new IllegalArgumentException("Valid personRequest was not provided: " + personRequest);
    }
  }
}
