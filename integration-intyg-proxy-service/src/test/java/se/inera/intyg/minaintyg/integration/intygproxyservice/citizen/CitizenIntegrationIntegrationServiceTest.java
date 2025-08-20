package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.citizen.model.Citizen;
import se.inera.intyg.minaintyg.integration.api.citizen.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.CitizenResponseDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.GetCitizenFromIntygProxyServiceImpl;
import se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class CitizenIntegrationIntegrationServiceTest {

  @Mock
  private GetCitizenFromIntygProxyServiceImpl getCitizenFromIntygProxyService;

  @Mock
  private CitizenResponseConverter citizenResponseConverter;

  @InjectMocks
  private CitizenIntegrationIntegrationService citizenIntegrationIntegrationService;

  private static final String CITIZEN_ID = "191212121212";
  private static final String CITIZEN_NAME = "citizenName";

  @Nested
  class ErrorHandlingTest {

    @Test
    void shouldThrowIlligalArgumentExceptionIfCitizenRequestIsNull() {
      assertThrows(IllegalArgumentException.class,
          () -> citizenIntegrationIntegrationService.getCitizen(null)
      );
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfCitizenRequestContainsNullCitizenId() {
      final var personRequest = GetCitizenIntegrationRequest.builder().citizenId(null).build();
      assertThrows(IllegalArgumentException.class,
          () -> citizenIntegrationIntegrationService.getCitizen(personRequest)
      );
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfCitizenRequestContainsEmptyCitizenId() {
      final var personRequest = GetCitizenIntegrationRequest.builder().citizenId("").build();
      assertThrows(IllegalArgumentException.class,
          () -> citizenIntegrationIntegrationService.getCitizen(personRequest)
      );
    }

    @Test
    void shouldReturnStatusErrorIfCommunicationErrorWithIntygProxyOccurs() {
      final var personRequest = GetCitizenIntegrationRequest.builder().citizenId(CITIZEN_ID).build();
      when(getCitizenFromIntygProxyService.getCitizenFromIntygProxy(personRequest)).thenThrow(
          RuntimeException.class);
      assertThrows(RuntimeException.class,
          () -> citizenIntegrationIntegrationService.getCitizen(personRequest)
      );
    }
  }

  @Nested
  class CitizenResponseTest {

    @Test
    void shouldReturnCitizenResponse() {
      final var personRequest = GetCitizenIntegrationRequest.builder().citizenId(CITIZEN_ID).build();
      final var personSvarDTO = getCitizenResponse();
      when(getCitizenFromIntygProxyService.getCitizenFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      final var actualResult = citizenIntegrationIntegrationService.getCitizen(personRequest);
      assertEquals(GetCitizenIntegrationResponse.class, actualResult.getClass());
    }

    @Test
    void shouldReturnCitizenResponseWithConvertedCitizen() {
      final var personRequest = GetCitizenIntegrationRequest.builder().citizenId(CITIZEN_ID).build();
      final var expectedResult = getCitizen();
      final var personSvarDTO = getCitizenResponse();
      when(getCitizenFromIntygProxyService.getCitizenFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      when(citizenResponseConverter.convertCitizen(personSvarDTO.getCitizen())).thenReturn(
          expectedResult);
      final var actualResult = citizenIntegrationIntegrationService.getCitizen(personRequest);
      assertEquals(expectedResult, actualResult.getCitizen());
    }


    @Test
    void shouldReturnCitizenResponseWithConvertedStatus() {
      final var personRequest = GetCitizenIntegrationRequest.builder().citizenId(CITIZEN_ID).build();
      final var expectedResult = Status.FOUND;
      final var personSvarDTO = getCitizenResponse();
      when(getCitizenFromIntygProxyService.getCitizenFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      when(citizenResponseConverter.convertStatus(personSvarDTO.getStatus())).thenReturn(
          expectedResult);
      final var actualResult = citizenIntegrationIntegrationService.getCitizen(personRequest);
      assertEquals(expectedResult, actualResult.getStatus());
    }
  }

  private static Citizen getCitizen() {
    return Citizen.builder()
        .name(CITIZEN_NAME)
        .citizenId(CITIZEN_ID)
        .build();
  }

  private static CitizenResponseDTO getCitizenResponse() {
    return CitizenResponseDTO.builder()
        .citizen(
            CitizenDTO.builder()
                .personnummer(CITIZEN_ID)
                .build()
        )
        .status(StatusDTO.FOUND)
        .build();
  }

}