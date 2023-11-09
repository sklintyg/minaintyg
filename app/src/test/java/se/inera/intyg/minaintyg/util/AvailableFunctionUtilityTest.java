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
  class TestIncludesFunction {

    @Test
    void shouldReturnTrueIfIncludedInList() {
      final var response = AvailableFunctionUtility.includesFunction(
          List.of(AvailableFunction.builder().type(AvailableFunctionType.SEND_CERTIFICATE).build()),
          AvailableFunctionType.SEND_CERTIFICATE
      );

      assertTrue(response);
    }

    @Test
    void shouldReturnFalseIfNotIncludedInList() {
      final var response = AvailableFunctionUtility.includesFunction(
          List.of(
              AvailableFunction.builder().type(AvailableFunctionType.PRINT_CERTIFICATE).build()),
          AvailableFunctionType.SEND_CERTIFICATE
      );

      assertFalse(response);
    }

    @Test
    void shouldReturnFalseIfEmptyList() {
      final var response = AvailableFunctionUtility.includesFunction(
          Collections.emptyList(),
          AvailableFunctionType.SEND_CERTIFICATE
      );

      assertFalse(response);
    }
  }

}