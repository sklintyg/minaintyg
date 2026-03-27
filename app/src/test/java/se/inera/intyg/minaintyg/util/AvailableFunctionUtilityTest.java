/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
      final var response =
          AvailableFunctionUtility.includesEnabledFunction(
              List.of(
                  AvailableFunction.builder()
                      .type(AvailableFunctionType.SEND_CERTIFICATE)
                      .enabled(true)
                      .build()),
              AvailableFunctionType.SEND_CERTIFICATE);

      assertTrue(response);
    }

    @Test
    void shouldReturnFalseIfIncludedInListAndDisabled() {
      final var response =
          AvailableFunctionUtility.includesEnabledFunction(
              List.of(
                  AvailableFunction.builder()
                      .type(AvailableFunctionType.SEND_CERTIFICATE)
                      .enabled(false)
                      .build()),
              AvailableFunctionType.SEND_CERTIFICATE);

      assertFalse(response);
    }

    @Test
    void shouldReturnFalseIfNotIncludedInList() {
      final var response =
          AvailableFunctionUtility.includesEnabledFunction(
              List.of(
                  AvailableFunction.builder()
                      .type(AvailableFunctionType.PRINT_CERTIFICATE)
                      .build()),
              AvailableFunctionType.SEND_CERTIFICATE);

      assertFalse(response);
    }

    @Test
    void shouldReturnFalseIfEmptyList() {
      final var response =
          AvailableFunctionUtility.includesEnabledFunction(
              Collections.emptyList(), AvailableFunctionType.SEND_CERTIFICATE);

      assertFalse(response);
    }
  }
}
