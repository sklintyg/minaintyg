package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonIntegrationService;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.api.person.Status;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailServiceImplTest {

    private static final String PERSON_ID = "191212121212";
    private static final String PERSON_FIRSTNAME = "Arnold";
    private static final String PERSON_LASTNAME = "Johansson";
    private static final String PERSON_NAME = "Arnold Johansson";
    @Mock
    private PersonIntegrationService personIntegrationService;
    @InjectMocks
    private MinaIntygUserDetailServiceImpl minaIntygUserDetailService;

    @Test
    void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.getPrincipal(null));
    }

    @Test
    void shouldThrowPersonNotFoundExceptionIfResponseHasStatusNotFound() {
        final var puResponse = getPuResponse(Status.NOT_FOUND);
        when(personIntegrationService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
        assertThrows(PersonNotFoundException.class, () -> minaIntygUserDetailService.getPrincipal(PERSON_ID));
    }

    @Test
    void shouldThrowPUServiceExceptionIfResponseHasStatusError() {
        final var puResponse = getPuResponse(Status.ERROR);
        when(personIntegrationService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
        assertThrows(PUServiceException.class, () -> minaIntygUserDetailService.getPrincipal(PERSON_ID));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsEmtpy() {
        assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.getPrincipal(""));
    }

    @Test
    void shouldReturnTypeMinaIntygUser() {
        final var puResponse = getPuResponse(Status.FOUND);
        when(personIntegrationService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
        final var principal = minaIntygUserDetailService.getPrincipal(PERSON_ID);
        assertEquals(principal.getClass(), MinaIntygUser.class);
    }

    @Test
    void shouldSetPersonIdFromPUResponseToUserObject() {
        final var puResponse = getPuResponse(Status.FOUND);
        when(personIntegrationService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
        final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID);
        assertEquals(PERSON_ID, principal.getPatientId());
    }

    @Test
    void shouldSetNameFromPUResponseToUserObject() {
        final var puResponse = getPuResponse(Status.FOUND);
        when(personIntegrationService.getPerson(any(PersonRequest.class))).thenReturn(puResponse);
        final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID);
        assertEquals(PERSON_NAME, principal.getPatientName());
    }

    private static PersonResponse getPuResponse(Status status) {
        return PersonResponse.builder()
            .person(
                Person.builder()
                    .personnummer(PERSON_ID)
                    .fornamn(PERSON_FIRSTNAME)
                    .efternamn(PERSON_LASTNAME)
                    .build()
            )
            .status(status)
            .build();
    }
}