package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.MessageLevel;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Alert.AlertBuilder;

@JsonDeserialize(builder = AlertBuilder.class)
@Value
@Builder
public class Alert {

  MessageLevel type;
  String text;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AlertBuilder {

  }
}