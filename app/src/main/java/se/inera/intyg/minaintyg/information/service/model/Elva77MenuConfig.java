package se.inera.intyg.minaintyg.information.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig.Elva77MenuConfigBuilder;

@Value
@Builder
@JsonDeserialize(builder = Elva77MenuConfigBuilder.class)
public class Elva77MenuConfig {

  String version;
  Elva77Menu menu;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Elva77MenuConfigBuilder {

  }
}
