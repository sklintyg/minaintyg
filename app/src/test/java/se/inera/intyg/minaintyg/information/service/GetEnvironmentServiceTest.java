package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetEnvironmentServiceTest {

  private static final String EXPECTED_ENVIRONMENT = "staging";

  @Mock
  GetEnvironmentService getEnvironmentService;

  @Test
  void shouldReturnConfigEnvironment() {
    when(getEnvironmentService.get()).thenReturn(EXPECTED_ENVIRONMENT);

    final var response = getEnvironmentService.get();

    assertEquals(EXPECTED_ENVIRONMENT, response);
  }

  @Test
  void shouldReturnNullWhenEnvironmentIsBlank() {
    when(getEnvironmentService.get()).thenReturn(null);

    final var response = getEnvironmentService.get();

    assertNull(response);
  }
}
