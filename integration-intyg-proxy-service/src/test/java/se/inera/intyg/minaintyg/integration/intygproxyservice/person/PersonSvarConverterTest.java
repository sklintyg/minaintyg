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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.PersonDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.PersonSvarDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class PersonSvarConverterTest {

  private static final String PERSON_ID = "personId";
  private static final String FIRSTNAME = "firstname";
  private static final String LASTNAME = "lastNnme";
  private static final String SURNAME = "surname";

  private final PersonSvarConverter personConverterService = new PersonSvarConverter();

  @Nested
  class ConvertPersonName {

    @Test
    void shouldReturnPersonWithPersonId() {
      final var personResponse = getPersonResponse(null, null);
      final var result = personConverterService.convertPerson(personResponse.getPerson());
      assertEquals(PERSON_ID, result.getPersonId());
    }

    @Test
    void shouldReturnPersonWithPersonName() {
      final var personResponse = getPersonResponse(null, null);
      final var result = personConverterService.convertPerson(personResponse.getPerson());
      assertEquals(FIRSTNAME + " " + LASTNAME, result.getName());
    }

    @Test
    void shouldReturnPersonWithPersonNameIncludingSurname() {
      final var personResponse = getPersonResponse(SURNAME, null);
      final var result = personConverterService.convertPerson(personResponse.getPerson());
      assertEquals(FIRSTNAME + " " + SURNAME + " " + LASTNAME, result.getName());
    }
  }

  @Nested
  class ConvertStatus {

    @Test
    void shouldReturnPersonWithStatusFound() {
      final var personResponse = getPersonResponse(null, StatusDTO.FOUND);
      final var result = personConverterService.convertStatus(personResponse.getStatus());
      assertEquals(Status.FOUND, result);
    }

    @Test
    void shouldReturnPersonWithStatusNotFound() {
      final var personResponse = getPersonResponse(null, StatusDTO.NOT_FOUND);
      final var result = personConverterService.convertStatus(personResponse.getStatus());
      assertEquals(Status.NOT_FOUND, result);
    }

    @Test
    void shouldReturnPersonWithStatusError() {
      final var personResponse = getPersonResponse(null, StatusDTO.ERROR);
      final var result = personConverterService.convertStatus(personResponse.getStatus());
      assertEquals(Status.ERROR, result);
    }
  }

  private PersonSvarDTO getPersonResponse(String surname, StatusDTO statusDTO) {
    return PersonSvarDTO.builder()
        .person(
            PersonDTO.builder()
                .fornamn(FIRSTNAME)
                .mellannamn(surname)
                .efternamn(LASTNAME)
                .personnummer(PERSON_ID)
                .build())
        .status(statusDTO)
        .build();
  }
}
