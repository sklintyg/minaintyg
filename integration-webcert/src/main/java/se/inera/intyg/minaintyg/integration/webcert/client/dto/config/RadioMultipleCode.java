package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCode.RadioMultipleCodeBuilder;

@JsonDeserialize(builder = RadioMultipleCodeBuilder.class)
@Value
@Builder
public class RadioMultipleCode {

  String id;
  String label;

  @JsonPOJOBuilder(withPrefix = "")
  public static class RadioMultipleCodeBuilder {

  }
}
