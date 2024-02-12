package se.inera.intyg.minaintyg.integrationtest.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.PersonDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.PersonSvarDTO;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@RequiredArgsConstructor
public class IntygProxyServiceMock {

  private final MockServerClient mockServerClient;

  public static final PersonDTO ATHENA_REACT_ANDERSSON = PersonDTO.builder()
      .personnummer("194011306125")
      .namn("Athena React Andersson")
      .fornamn("Athena")
      .mellannamn("React")
      .efternamn("Andersson")
      .build();

  public void foundPerson(PersonDTO person) {
    try {
      mockServerClient.when(HttpRequest.request("/api/v1/person"))
          .respond(
              HttpResponse
                  .response(
                      new ObjectMapper().writeValueAsString(
                          PersonSvarDTO.builder()
                              .status(StatusDTO.FOUND)
                              .person(
                                  person
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
