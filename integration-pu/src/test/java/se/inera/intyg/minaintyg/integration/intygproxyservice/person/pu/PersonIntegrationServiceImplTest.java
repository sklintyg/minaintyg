package se.inera.intyg.minaintyg.integration.intygproxyservice.person.pu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.person.Person;
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.PersonIntegrationServiceImpl;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetPersonFromIntygProxyService;

@ExtendWith(MockitoExtension.class)
class PersonIntegrationServiceImplTest {

    @Mock
    private GetPersonFromIntygProxyService getPersonFromIntygProxyService;

    @InjectMocks
    private PersonIntegrationServiceImpl personIntegrationService;

    private static final String PERSON_ID = "191212121212";

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
        when(getPersonFromIntygProxyService.getPerson(personRequest)).thenReturn(expectedResult);
        final var actualResult = personIntegrationService.getPerson(personRequest);
        assertEquals(expectedResult, actualResult);
    }
}