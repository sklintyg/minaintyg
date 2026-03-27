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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenResponseDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class CitizenResponseConverterTest {

  private static final String PERSON_ID = "personId";
  private static final String FIRSTNAME = "firstname";
  private static final String LASTNAME = "lastNnme";
  private static final boolean IS_ACTIVE = true;

  private final CitizenResponseConverter citizenResponseConverter = new CitizenResponseConverter();

  @Nested
  class ConvertPersonName {

    @Test
    void shouldReturnPersonWithPersonId() {
      final var citizenResponse = getCitizenResponse(null);
      final var result = citizenResponseConverter.convertCitizen(citizenResponse.getCitizen());
      assertEquals(PERSON_ID, result.getPersonId());
    }

    @Test
    void shouldReturnPersonWithIsActiveTrue() {
      final var citizenResponse = getCitizenResponse(null);
      final var result = citizenResponseConverter.convertCitizen(citizenResponse.getCitizen());
      assertEquals(IS_ACTIVE, result.getIsActive());
    }

    @Test
    void shouldReturnPersonWithName() {
      final var citizenResponse = getCitizenResponse(null);
      final var result = citizenResponseConverter.convertCitizen(citizenResponse.getCitizen());
      assertEquals(FIRSTNAME + " " + LASTNAME, result.getName());
    }
  }

  @Nested
  class ConvertStatus {

    @Test
    void shouldReturnCitizenWithStatusFound() {
      final var citizenResponse = getCitizenResponse(StatusDTO.FOUND);
      final var result = citizenResponseConverter.convertStatus(citizenResponse.getStatus());
      assertEquals(Status.FOUND, result);
    }

    @Test
    void shouldReturnCitizenWithStatusNotFound() {
      final var citizenResponse = getCitizenResponse(StatusDTO.NOT_FOUND);
      final var result = citizenResponseConverter.convertStatus(citizenResponse.getStatus());
      assertEquals(Status.NOT_FOUND, result);
    }

    @Test
    void shouldReturnCitizenWithStatusError() {
      final var citizenResponse = getCitizenResponse(StatusDTO.ERROR);
      final var result = citizenResponseConverter.convertStatus(citizenResponse.getStatus());
      assertEquals(Status.ERROR, result);
    }
  }

  private CitizenResponseDTO getCitizenResponse(StatusDTO statusDTO) {
    return CitizenResponseDTO.builder()
        .citizen(
            CitizenDTO.builder()
                .fornamn(FIRSTNAME)
                .efternamn(LASTNAME)
                .personnummer(PERSON_ID)
                .isActive(IS_ACTIVE)
                .build())
        .status(statusDTO)
        .build();
  }
}
