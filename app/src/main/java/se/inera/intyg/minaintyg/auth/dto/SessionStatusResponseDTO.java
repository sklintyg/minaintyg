package se.inera.intyg.minaintyg.auth.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SessionStatusResponseDTO {

  private boolean hasSession;
  private long secondsUntilExpire;
}
