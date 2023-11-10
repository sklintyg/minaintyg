package se.inera.intyg.minaintyg.integration.common;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
@Builder
public class IntegrationServiceException extends RuntimeException {

  String applicationName;
  String message;
}
