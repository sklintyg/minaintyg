package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate.CheckboxMultipleDateBuilder;

@JsonDeserialize(builder = CheckboxMultipleDateBuilder.class)
@Value
@Builder
public class CheckboxMultipleDate {

  String id;
  String label;
  LocalDate maxDate;
  LocalDate minDate;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CheckboxMultipleDateBuilder {

  }
}
