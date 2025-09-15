package se.inera.intyg.minaintyg.information.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink.DynamicLinkBuilder;

@Value
@Builder
@JsonDeserialize(builder = DynamicLinkBuilder.class)
public class DynamicLink {

  String id;
  String name;
  Map<String, String> url;
  boolean isAgentModeSupported;

  @JsonPOJOBuilder(withPrefix = "")
  public static class DynamicLinkBuilder {

  }
}
