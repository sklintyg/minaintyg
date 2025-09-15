package se.inera.intyg.minaintyg.information.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Elva77MenuConfig.Elva77MenuConfigBuilder.class)
public class Elva77MenuConfig {

  String version;
  Elva77Menu menu;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Elva77MenuConfigBuilder {

  }
}
