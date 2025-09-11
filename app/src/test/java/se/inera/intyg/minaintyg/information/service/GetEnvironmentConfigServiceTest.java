package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class GetEnvironmentConfigServiceTest {

  private static final String EXPECTED_ENVIRONMENT = "staging";

  private final GetEnvironmentConfigService getEnvironmentConfigService = new GetEnvironmentConfigService();

  @Test
  void shouldReturnConfigEnvironment() {
    ReflectionTestUtils.setField(getEnvironmentConfigService, "environmentType",
        EXPECTED_ENVIRONMENT);

    final var response = getEnvironmentConfigService.get();

    assertEquals(EXPECTED_ENVIRONMENT, response);
  }
}
