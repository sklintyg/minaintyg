package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionTimeoutFilterTest {

  private static final List<String> SKIP_RENEW_URLS = List.of("/test");

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  FilterChain filterChain;

  @Mock
  SessionTimeoutService sessionTimeoutService;
  @InjectMocks
  SessionTimeoutFilter filter;

  @BeforeEach
  void setup() {
    filter.setSkipRenewSessionUrls(SKIP_RENEW_URLS);
  }

  @Test
  void shouldCallServiceWithRequest() throws ServletException, IOException {
    final var captor = ArgumentCaptor.forClass(HttpServletRequest.class);

    filter.doFilterInternal(request, response, filterChain);

    verify(sessionTimeoutService).checkSessionValidity(captor.capture(), anyList());
    assertEquals(request, captor.getValue());
  }

  @Test
  void shouldCallServiceWithSkipUrls() throws ServletException, IOException {
    final var captor = ArgumentCaptor.forClass(List.class);

    filter.doFilterInternal(request, response, filterChain);

    verify(sessionTimeoutService)
        .checkSessionValidity(any(HttpServletRequest.class), captor.capture());
    assertEquals(SKIP_RENEW_URLS, captor.getValue());
  }

  @Test
  void shouldCallFilterChainWithRequest() throws ServletException, IOException {
    final var captor = ArgumentCaptor.forClass(HttpServletRequest.class);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(captor.capture(), any());
    assertEquals(request, captor.getValue());
  }

  @Test
  void shouldCallFilterChainWithResponse() throws ServletException, IOException {
    final var captor = ArgumentCaptor.forClass(HttpServletResponse.class);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(any(), captor.capture());
    assertEquals(response, captor.getValue());
  }
}