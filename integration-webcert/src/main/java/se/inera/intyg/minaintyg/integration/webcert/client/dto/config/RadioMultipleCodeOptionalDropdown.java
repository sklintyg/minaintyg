package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCodeOptionalDropdown.RadioMultipleCodeOptionalDropdownBuilder;

@JsonDeserialize(builder = RadioMultipleCodeOptionalDropdownBuilder.class)
@Value
@Builder
public class RadioMultipleCodeOptionalDropdown {

  String id;
  String label;
  String dropdownQuestionId;

  @JsonPOJOBuilder(withPrefix = "")
  public static class RadioMultipleCodeOptionalDropdownBuilder {

  }
}
