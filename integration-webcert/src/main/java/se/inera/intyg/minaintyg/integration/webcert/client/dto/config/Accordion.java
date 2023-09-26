package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.Accordion.AccordionBuilder;

@JsonDeserialize(builder = AccordionBuilder.class)
@Value
@Builder
public class Accordion {

  String openText;
  String closeText;
  String header;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AccordionBuilder {

  }
}
