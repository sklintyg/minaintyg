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
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyServiceImpl;

@ExtendWith(MockitoExtension.class)
class PersonIntegrationServiceTest {

  @Mock
  private GetPersonFromIntygProxyServiceImpl getPersonFromIntygProxyService;

  @InjectMocks
  private PersonIntegrationService personIntegrationService;

  private static final String PERSON_ID = "191212121212";

  @Nested
  class ErrorHandling {

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestIsNull() {
      assertThrows(IllegalArgumentException.class, () -> personIntegrationService.getPerson(null));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestContainsNullPersonId() {
      final var personRequest = PersonRequest.builder().personId(null).build();
      assertThrows(IllegalArgumentException.class,
          () -> personIntegrationService.getPerson(personRequest));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestContainsEmptyPersonId() {
      final var personRequest = PersonRequest.builder().personId("").build();
      assertThrows(IllegalArgumentException.class,
          () -> personIntegrationService.getPerson(personRequest));
    }

    @Test
    void shouldThrowRuntimeExceptionIfCommunicationErrorWithIntygProxyOccurs() {
      final var personRequest = PersonRequest.builder().personId(PERSON_ID).build();
      when(getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest)).thenThrow(
          RuntimeException.class);
      assertThrows(RuntimeException.class, () -> personIntegrationService.getPerson(personRequest));
    }
  }

  @Test
  void shouldReturnPersonResponse() {
    final var personRequest = PersonRequest.builder().personId(PERSON_ID).build();
    final var expectedResult = PersonResponse.builder()
        .person(
            Person.builder()
                .personnummer(PERSON_ID)
                .build()
        )
        .build();
    when(getPersonFromIntygProxyService.getPersonFromIntygProxy(personRequest)).thenReturn(
        expectedResult);
    final var actualResult = personIntegrationService.getPerson(personRequest);
    assertEquals(expectedResult, actualResult);
  }
}