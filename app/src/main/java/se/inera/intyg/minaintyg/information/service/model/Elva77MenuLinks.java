package se.inera.intyg.minaintyg.information.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuLinks.Elva77MenuLinksBuilder;

@Value
@Builder
@JsonDeserialize(builder = Elva77MenuLinksBuilder.class)
public class Elva77MenuLinks {

  String id;
  String name;
  Map<String, String> url;
  boolean isAgentModeSupported;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Elva77MenuLinksBuilder {

  }
}
