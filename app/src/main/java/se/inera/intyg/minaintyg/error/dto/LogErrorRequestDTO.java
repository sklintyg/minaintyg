package se.inera.intyg.minaintyg.error.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogErrorRequestDTO {

  private ErrorData error;
}
