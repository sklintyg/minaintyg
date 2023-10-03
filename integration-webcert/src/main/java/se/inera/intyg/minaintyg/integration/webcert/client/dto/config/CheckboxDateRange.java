package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxDateRange.CheckboxDateRangeBuilder;

@JsonDeserialize(builder = CheckboxDateRangeBuilder.class)
@Value
@Builder
public class CheckboxDateRange {

  String id;
  String label;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CheckboxDateRangeBuilder {

  }
}
