package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.X_FRAME_OPTIONS;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class CustomXFrameOptionsHeaderWriterTest {

  private static final String POST = "POST";
  private static final String REQUEST_URI_PDF = "/pdf";
  private static final String NOT_REQUEST_URI_PDF = "/other";
  private HttpServletRequest request;
  private HttpServletResponse response;
  private CustomXFrameOptionsHeaderWriter customXFrameOptionsHeaderWriter;

  @BeforeEach
  void setUp() {
    customXFrameOptionsHeaderWriter = new CustomXFrameOptionsHeaderWriter();
    response = new MockHttpServletResponse();
  }

  @Test
  void shouldSetFrameOptionsToSameOrigin() {
    request = new MockHttpServletRequest(POST, REQUEST_URI_PDF);
    customXFrameOptionsHeaderWriter.writeHeaders(request, response);
    assertEquals(List.of("SAMEORIGIN"), response.getHeaders(X_FRAME_OPTIONS));
  }

  @Test
  void shouldSetFrameOptionsToDeny() {
    request = new MockHttpServletRequest(POST, NOT_REQUEST_URI_PDF);
    customXFrameOptionsHeaderWriter.writeHeaders(request, response);
    assertEquals(List.of("DENY"), response.getHeaders(X_FRAME_OPTIONS));
  }
}
