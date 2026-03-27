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
package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenResponseDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.GetCitizenFromIntygProxyServiceImpl;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class CitizenIntegrationIntegrationServiceTest {

  @Mock private GetCitizenFromIntygProxyServiceImpl getCitizenFromIntygProxyService;

  @Mock private CitizenResponseConverter citizenResponseConverter;

  @InjectMocks private CitizenIntegrationIntegrationService citizenIntegrationIntegrationService;

  private static final String CITIZEN_ID = "191212121212";
  private static final String CITIZEN_NAME = "citizenName";

  @Nested
  class ErrorHandlingTest {

    @Test
    void shouldThrowIlligalArgumentExceptionIfCitizenRequestIsNull() {
      assertThrows(
          IllegalArgumentException.class,
          () -> citizenIntegrationIntegrationService.getPerson(null));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfCitizenRequestContainsNullCitizenId() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(null).build();
      assertThrows(
          IllegalArgumentException.class,
          () -> citizenIntegrationIntegrationService.getPerson(personRequest));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfCitizenRequestContainsEmptyCitizenId() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId("").build();
      assertThrows(
          IllegalArgumentException.class,
          () -> citizenIntegrationIntegrationService.getPerson(personRequest));
    }
  }

  @Nested
  class CitizenResponseTest {

    @Test
    void shouldReturnCitizenResponse() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(CITIZEN_ID).build();
      final var citizenRequest =
          GetCitizenIntegrationRequest.builder().personId(CITIZEN_ID).build();
      final var citizenResponse = getCitizenResponse();
      when(getCitizenFromIntygProxyService.getCitizenFromIntygProxy(citizenRequest))
          .thenReturn(citizenResponse);
      final var actualResult = citizenIntegrationIntegrationService.getPerson(personRequest);
      assertEquals(GetCitizenIntegrationResponse.class, actualResult.getClass());
    }

    @Test
    void shouldReturnCitizenResponseWithConvertedCitizen() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(CITIZEN_ID).build();
      final var citizenRequest =
          GetCitizenIntegrationRequest.builder().personId(CITIZEN_ID).build();
      final var expectedResult = getCitizen();
      final var personSvarDTO = getCitizenResponse();
      when(getCitizenFromIntygProxyService.getCitizenFromIntygProxy(citizenRequest))
          .thenReturn(personSvarDTO);
      when(citizenResponseConverter.convertCitizen(personSvarDTO.getCitizen()))
          .thenReturn(expectedResult);
      final var actualResult = citizenIntegrationIntegrationService.getPerson(personRequest);
      assertEquals(expectedResult, actualResult.getPerson());
    }

    @Test
    void shouldReturnCitizenResponseWithConvertedStatus() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(CITIZEN_ID).build();
      final var citizenRequest =
          GetCitizenIntegrationRequest.builder().personId(CITIZEN_ID).build();
      final var expectedResult = Status.FOUND;
      final var personSvarDTO = getCitizenResponse();
      when(getCitizenFromIntygProxyService.getCitizenFromIntygProxy(citizenRequest))
          .thenReturn(personSvarDTO);
      when(citizenResponseConverter.convertStatus(personSvarDTO.getStatus()))
          .thenReturn(expectedResult);
      final var actualResult = citizenIntegrationIntegrationService.getPerson(personRequest);
      assertEquals(expectedResult, actualResult.getStatus());
    }
  }

  private static Person getCitizen() {
    return Person.builder().name(CITIZEN_NAME).personId(CITIZEN_ID).build();
  }

  private static CitizenResponseDTO getCitizenResponse() {
    return CitizenResponseDTO.builder()
        .citizen(CitizenDTO.builder().personnummer(CITIZEN_ID).build())
        .status(StatusDTO.FOUND)
        .build();
  }
}
