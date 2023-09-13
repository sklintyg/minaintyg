package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;

@ExtendWith(MockitoExtension.class)
class PersonConverterServiceImplTest {

  private static final String PERSON_ID = "personId";
  private static final String FIRSTNAME = "firstname";
  private static final String LASTNAME = "lastNnme";
  private static final String SURNAME = "surname";

  private final PersonConverterServiceImpl personConverterService = new PersonConverterServiceImpl();

  @Nested
  class ConvertPersonName {

    @Test
    void shouldReturnPersonWithPersonIdFromPersonResponse() {
      final var personResponse = getPersonResponse(null);
      final var result = personConverterService.convert(personResponse.getPerson());
      assertEquals(PERSON_ID, result.getPersonnummer());
    }

    @Test
    void shouldReturnPersonWithPersonNameFromPersonResponse() {
      final var personResponse = getPersonResponse(null);
      final var result = personConverterService.convert(personResponse.getPerson());
      assertEquals(FIRSTNAME + " " + LASTNAME, result.getNamn());
    }

    @Test
    void shouldReturnPersonWithPersonNameIncludedSurnameFromPersonResponse() {
      final var personResponse = getPersonResponse(SURNAME);
      final var result = personConverterService.convert(personResponse.getPerson());
      assertEquals(FIRSTNAME + " " + SURNAME + " " + LASTNAME, result.getNamn());
    }


    private PersonResponse getPersonResponse(String surname) {
      return PersonResponse.builder()
          .person(
              Person.builder()
                  .fornamn(FIRSTNAME)
                  .mellannamn(surname)
                  .efternamn(LASTNAME)
                  .personnummer(PERSON_ID)
                  .build()
          )
          .build();
    }
  }
}