package se.inera.intyg.minaintyg.integrationtest.environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@RequiredArgsConstructor
public class IntygsadminMock {

  private final MockServerClient mockServerClient;

  public void emptyBanners() {
    try {
      mockServerClient.when(HttpRequest.request("/actuator/banner/MINA_INTYG"))
          .respond(
              HttpResponse
                  .response(
                      JsonMapper.builder()
                          .addModule(new JavaTimeModule())
                          .build()
                          .writeValueAsString(
                              Collections.emptyList()
                          )
                  )
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON)
          );
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException(ex);
    }
  }

  public void foundBanners(BannerDTO... bannerDTOS) {
    try {
      mockServerClient.when(HttpRequest.request("/actuator/banner/MINA_INTYG"))
          .respond(
              HttpResponse
                  .response(
                      JsonMapper.builder()
                          .addModule(new JavaTimeModule())
                          .build()
                          .writeValueAsString(
                              bannerDTOS
                          )
                  )
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON)
          );
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
