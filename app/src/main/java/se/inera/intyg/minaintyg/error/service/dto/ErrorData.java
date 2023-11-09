package se.inera.intyg.minaintyg.error.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorData {

  String id;
  String code;
  String message;
  String stackTrace;
}
