package se.inera.intyg.minaintyg.error.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LogErrorRequest {

  ErrorData error;
}
