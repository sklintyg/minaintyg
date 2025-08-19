package se.inera.intyg.minaintyg.integrationtest.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.UserDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.UserResponseDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@RequiredArgsConstructor
public class IntygProxyServiceMock {

  private final MockServerClient mockServerClient;

  public static final UserDTO ATHENA_REACT_ANDERSSON = UserDTO.builder()
      .personnummer("194011306125")
      .namn("Athena React Andersson")
      .fornamn("Athena")
      .mellannamn("React")
      .efternamn("Andersson")
      .build();

  public void foundUser(UserDTO user) {
    try {
      mockServerClient.when(HttpRequest.request("/api/v1/user"))
          .respond(
              HttpResponse
                  .response(
                      new ObjectMapper().writeValueAsString(
                          UserResponseDTO.builder()
                              .status(StatusDTO.FOUND)
                              .user(
                                  user
                              )
                              .build()
                      )
                  )
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON)
          );
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
}
