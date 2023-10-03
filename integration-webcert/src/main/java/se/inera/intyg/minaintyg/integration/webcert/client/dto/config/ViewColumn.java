package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.ViewColumn.ViewColumnBuilder;

@JsonDeserialize(builder = ViewColumnBuilder.class)
@Value
@Builder
public class ViewColumn {

  String id;
  String text;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ViewColumnBuilder {

  }
}
