package se.inera.intyg.minaintyg.util;

import java.util.List;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunctionType;

public class AvailableFunctionUtility {

  private AvailableFunctionUtility() {

  }

  public static boolean includesEnabledFunction(
      List<AvailableFunction> functions,
      AvailableFunctionType type) {
    return functions.stream()
        .anyMatch(function -> function.getType() == type && function.isEnabled());
  }
}
