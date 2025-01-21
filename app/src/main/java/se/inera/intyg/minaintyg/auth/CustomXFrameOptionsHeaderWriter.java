package se.inera.intyg.minaintyg.auth;

import static org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.X_FRAME_OPTIONS;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.stereotype.Component;


@Component
public class CustomXFrameOptionsHeaderWriter implements HeaderWriter {

  private static final String PDF_API_IDENTIFIER = "/pdf";
  private static final String SAMEORIGIN = "SAMEORIGIN";
  private static final String DENY = "DENY";

  @Override
  public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
    if (isPdfRequest(request)) {
      response.setHeader(X_FRAME_OPTIONS, SAMEORIGIN);
    } else {
      response.setHeader(X_FRAME_OPTIONS, DENY);
    }
  }

  private boolean isPdfRequest(HttpServletRequest request) {
    return request.getRequestURI().contains(PDF_API_IDENTIFIER);
  }
}
