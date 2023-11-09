package se.inera.intyg.minaintyg.error.service.dto;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.error.dto.ErrorData;

@Value
@Builder
public class LogErrorRequest {

  ErrorData error;
}
