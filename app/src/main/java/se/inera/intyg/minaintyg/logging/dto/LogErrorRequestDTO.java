package se.inera.intyg.minaintyg.logging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogErrorRequestDTO {

  private String id;
  private String code;
  private String message;
  private String stackTrace;
}
