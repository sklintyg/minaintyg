package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.PUIntegrationService;
import se.inera.intyg.minaintyg.integration.dto.PUResponse;
import se.inera.intyg.minaintyg.integration.dto.Person;
import se.inera.intyg.minaintyg.integration.dto.Status;

@ExtendWith(MockitoExtension.class)
class MinaIntygUserDetailServiceImplTest {

    private static final String PERSON_ID = "191212121212";
    private static final String PERSON_NAME = "Arnold Johansson";
    @Mock
    private PUIntegrationService puIntegrationService;
    @InjectMocks
    private MinaIntygUserDetailServiceImpl minaIntygUserDetailService;
    @Test
    void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.getPrincipal(null));
    }

    @Test
    void shouldThrowPersonNotFoundExceptionIfResponseHasStatusNotFound() {
        final var puResponse = getPuResponse(Status.NOT_FOUND);
        when(puIntegrationService.get(PERSON_ID)).thenReturn(puResponse);
        assertThrows(PersonNotFoundException.class, () -> minaIntygUserDetailService.getPrincipal(PERSON_ID));
    }

    @Test
    void shouldThrowPUServiceExceptionIfResponseHasStatusError() {
        final var puResponse = getPuResponse(Status.ERROR);
        when(puIntegrationService.get(PERSON_ID)).thenReturn(puResponse);
        assertThrows(PUServiceException.class, () -> minaIntygUserDetailService.getPrincipal(PERSON_ID));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfNoPersonIdIsEmtpy() {
        assertThrows(IllegalArgumentException.class, () -> minaIntygUserDetailService.getPrincipal(""));
    }

    @Test
    void shouldReturnTypeMinaIntygUser() {
        final var puResponse = getPuResponse(Status.FOUND);
        when(puIntegrationService.get(PERSON_ID)).thenReturn(puResponse);
        final var principal = minaIntygUserDetailService.getPrincipal(PERSON_ID);
        assertEquals(principal.getClass(), MinaIntygUser.class);
    }

    @Test
    void shouldSetPersonIdFromPUResponseToUserObject() {
        final var puResponse = getPuResponse(Status.FOUND);
        when(puIntegrationService.get(PERSON_ID)).thenReturn(puResponse);
        final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID);
        assertEquals(PERSON_ID, principal.getPatientId());
    }

    @Test
    void shouldSetNameFromPUResponseToUserObject() {
        final var puResponse = getPuResponse(Status.FOUND);
        when(puIntegrationService.get(PERSON_ID)).thenReturn(puResponse);
        final var principal = (MinaIntygUser) minaIntygUserDetailService.getPrincipal(PERSON_ID);
        assertEquals(PERSON_NAME, principal.getPatientName());
    }

    private static PUResponse getPuResponse(Status status) {
        return PUResponse.builder()
            .person(
                Person.builder()
                    .personId(PERSON_ID)
                    .name(PERSON_NAME)
                    .build()
            )
            .status(status)
            .build();
    }
}