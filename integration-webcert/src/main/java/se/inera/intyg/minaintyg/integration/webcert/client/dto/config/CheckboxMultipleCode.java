package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleCode.CheckboxMultipleCodeBuilder;

@JsonDeserialize(builder = CheckboxMultipleCodeBuilder.class)
@Value
@Builder
public class CheckboxMultipleCode {

  String id;
  String label;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CheckboxMultipleCodeBuilder {

  }
}
