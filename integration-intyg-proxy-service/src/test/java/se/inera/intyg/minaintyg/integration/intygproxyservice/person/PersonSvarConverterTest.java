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
                .build()
        )
        .status(statusDTO)
        .build();
  }
}
