package se.inera.intyg.minaintyg.integration.intygproxyservice.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyServiceImpl;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.PersonDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.PersonSvarDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class PersonIntegrationIntegrationServiceTest {

  @Mock
  private GetPersonFromIntygProxyServiceImpl getPersonFromIntygProxyService;

  @Mock
  private PersonSvarConverter personSvarConverter;

  @InjectMocks
  private PersonIntegrationIntegrationService personIntegrationIntegrationService;

  private static final String PERSON_ID = "191212121212";
  private static final String PERSON_NAME = "personName";

  @Nested
  class ErrorHandlingTest {

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestIsNull() {
      assertThrows(IllegalArgumentException.class,
          () -> personIntegrationIntegrationService.getPerson(null)
      );
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestContainsNullPersonId() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(null).build();
      assertThrows(IllegalArgumentException.class,
          () -> personIntegrationIntegrationService.getPerson(personRequest)
      );
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestContainsEmptyPersonId() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId("").build();
      assertThrows(IllegalArgumentException.class,
          () -> personIntegrationIntegrationService.getPerson(personRequest)
      );
    }

    @Test
    void shouldReturnStatusErrorIfCommunicationErrorWithIntygProxyOccurs() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(PERSON_ID).build();
      when(getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest)).thenThrow(
          RuntimeException.class);
      assertThrows(RuntimeException.class,
          () -> personIntegrationIntegrationService.getPerson(personRequest)
      );
    }
  }

  @Nested
  class PersonResponseTest {

    @Test
    void shouldReturnPersonResponse() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(PERSON_ID).build();
      final var personSvarDTO = getPersonResponse();
      when(getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      final var actualResult = personIntegrationIntegrationService.getPerson(personRequest);
      assertEquals(GetPersonIntegrationResponse.class, actualResult.getClass());
    }

    @Test
    void shouldReturnPersonResponseWithConvertedPerson() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(PERSON_ID).build();
      final var expectedResult = getPerson();
      final var personSvarDTO = getPersonResponse();
      when(getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      when(personSvarConverter.convertPerson(personSvarDTO.getPerson())).thenReturn(
          expectedResult);
      final var actualResult = personIntegrationIntegrationService.getPerson(personRequest);
      assertEquals(expectedResult, actualResult.getPerson());
    }


    @Test
    void shouldReturnPersonResponseWithConvertedStatus() {
      final var personRequest = GetPersonIntegrationRequest.builder().personId(PERSON_ID).build();
      final var expectedResult = Status.FOUND;
      final var personSvarDTO = getPersonResponse();
      when(getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      when(personSvarConverter.convertStatus(personSvarDTO.getStatus())).thenReturn(
          expectedResult);
      final var actualResult = personIntegrationIntegrationService.getPerson(personRequest);
      assertEquals(expectedResult, actualResult.getStatus());
    }
  }

  private static Person getPerson() {
    return Person.builder()
        .name(PERSON_NAME)
        .personId(PERSON_ID)
        .build();
  }

  private static PersonSvarDTO getPersonResponse() {
    return PersonSvarDTO.builder()
        .person(
            PersonDTO.builder()
                .personnummer(PERSON_ID)
                .build()
        )
        .status(StatusDTO.FOUND)
        .build();
  }
}
