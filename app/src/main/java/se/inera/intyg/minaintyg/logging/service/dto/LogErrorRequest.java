package se.inera.intyg.minaintyg.logging.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LogErrorRequest {

  String id;
  String code;
  String message;
  String stackTrace;
}
