package se.inera.intyg.minaintyg.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunctionType;

class AvailableFunctionUtilityTest {

  @Nested
  class TestIncludesEnabledFunction {

    @Test
    void shouldReturnTrueIfIncludedInListAndEnabled() {
      final var response = AvailableFunctionUtility.includesEnabledFunction(
          List.of(
              AvailableFunction.builder()
                  .type(AvailableFunctionType.SEND_CERTIFICATE)
                  .enabled(true)
                  .build()),
          AvailableFunctionType.SEND_CERTIFICATE
      );

      assertTrue(response);
    }

    @Test
    void shouldReturnFalseIfIncludedInListAndDisabled() {
      final var response = AvailableFunctionUtility.includesEnabledFunction(
          List.of(AvailableFunction.builder()
              .type(AvailableFunctionType.SEND_CERTIFICATE)
              .enabled(false).build()),
          AvailableFunctionType.SEND_CERTIFICATE
      );

      assertFalse(response);
    }

    @Test
    void shouldReturnFalseIfNotIncludedInList() {
      final var response = AvailableFunctionUtility.includesEnabledFunction(
          List.of(
              AvailableFunction.builder().type(AvailableFunctionType.PRINT_CERTIFICATE).build()),
          AvailableFunctionType.SEND_CERTIFICATE
      );

      assertFalse(response);
    }

    @Test
    void shouldReturnFalseIfEmptyList() {
      final var response = AvailableFunctionUtility.includesEnabledFunction(
          Collections.emptyList(),
          AvailableFunctionType.SEND_CERTIFICATE
      );

      assertFalse(response);
    }
  }

}