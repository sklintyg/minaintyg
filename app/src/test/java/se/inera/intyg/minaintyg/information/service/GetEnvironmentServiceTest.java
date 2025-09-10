package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class GetEnvironmentServiceTest {

  private static final String EXPECTED_ENVIRONMENT = "staging";

  private final GetEnvironmentService getEnvironmentService = new GetEnvironmentService();

  @Test
  void shouldReturnConfigEnvironment() {
    ReflectionTestUtils.setField(getEnvironmentService, "environmentType", EXPECTED_ENVIRONMENT);

    final var response = getEnvironmentService.get();

    assertEquals(EXPECTED_ENVIRONMENT, response);
  }
}
