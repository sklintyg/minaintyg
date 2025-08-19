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
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.model.User;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.GetUserFromIntygProxyServiceImpl;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.UserDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.UserResponseDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class UserIntegrationIntegrationServiceTest {

  @Mock
  private GetUserFromIntygProxyServiceImpl getPersonFromIntygProxyService;

  @Mock
  private UserResponseConverter userResponseConverter;

  @InjectMocks
  private UserIntegrationIntegrationService userIntegrationIntegrationService;

  private static final String PERSON_ID = "191212121212";
  private static final String PERSON_NAME = "personName";

  @Nested
  class ErrorHandlingTest {

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestIsNull() {
      assertThrows(IllegalArgumentException.class,
          () -> userIntegrationIntegrationService.getUser(null)
      );
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestContainsNullPersonId() {
      final var personRequest = GetUserIntegrationRequest.builder().userId(null).build();
      assertThrows(IllegalArgumentException.class,
          () -> userIntegrationIntegrationService.getUser(personRequest)
      );
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfPersonRequestContainsEmptyPersonId() {
      final var personRequest = GetUserIntegrationRequest.builder().userId("").build();
      assertThrows(IllegalArgumentException.class,
          () -> userIntegrationIntegrationService.getUser(personRequest)
      );
    }

    @Test
    void shouldReturnStatusErrorIfCommunicationErrorWithIntygProxyOccurs() {
      final var personRequest = GetUserIntegrationRequest.builder().userId(PERSON_ID).build();
      when(getPersonFromIntygProxyService.getUserFromIntygProxy(personRequest)).thenThrow(
          RuntimeException.class);
      assertThrows(RuntimeException.class,
          () -> userIntegrationIntegrationService.getUser(personRequest)
      );
    }
  }

  @Nested
  class UserResponseTest {

    @Test
    void shouldReturnPersonResponse() {
      final var personRequest = GetUserIntegrationRequest.builder().userId(PERSON_ID).build();
      final var personSvarDTO = getPersonResponse();
      when(getPersonFromIntygProxyService.getUserFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      final var actualResult = userIntegrationIntegrationService.getUser(personRequest);
      assertEquals(GetUserIntegrationResponse.class, actualResult.getClass());
    }

    @Test
    void shouldReturnPersonResponseWithConvertedPerson() {
      final var personRequest = GetUserIntegrationRequest.builder().userId(PERSON_ID).build();
      final var expectedResult = getUser();
      final var personSvarDTO = getPersonResponse();
      when(getPersonFromIntygProxyService.getUserFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      when(userResponseConverter.convertUser(personSvarDTO.getUser())).thenReturn(
          expectedResult);
      final var actualResult = userIntegrationIntegrationService.getUser(personRequest);
      assertEquals(expectedResult, actualResult.getUser());
    }


    @Test
    void shouldReturnPersonResponseWithConvertedStatus() {
      final var personRequest = GetUserIntegrationRequest.builder().userId(PERSON_ID).build();
      final var expectedResult = Status.FOUND;
      final var personSvarDTO = getPersonResponse();
      when(getPersonFromIntygProxyService.getUserFromIntygProxy(personRequest)).thenReturn(
          personSvarDTO);
      when(userResponseConverter.convertStatus(personSvarDTO.getStatus())).thenReturn(
          expectedResult);
      final var actualResult = userIntegrationIntegrationService.getUser(personRequest);
      assertEquals(expectedResult, actualResult.getStatus());
    }
  }

  private static User getUser() {
    return User.builder()
        .name(PERSON_NAME)
        .userId(PERSON_ID)
        .build();
  }

  private static UserResponseDTO getPersonResponse() {
    return UserResponseDTO.builder()
        .user(
            UserDTO.builder()
                .personnummer(PERSON_ID)
                .build()
        )
        .status(StatusDTO.FOUND)
        .build();
  }
}
