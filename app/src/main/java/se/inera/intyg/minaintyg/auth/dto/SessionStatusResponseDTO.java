package se.inera.intyg.minaintyg.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionStatusResponseDTO {

  private boolean hasSession;
  private boolean isAuthenticated;
  private long secondsUntilExpire;
}
