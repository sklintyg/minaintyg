package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.VisualAcuity.VisualAcuityBuilder;

@JsonDeserialize(builder = VisualAcuityBuilder.class)
@Value
@Builder
public class VisualAcuity {

  String label;
  String withCorrectionId;
  String withoutCorrectionId;
  String contactLensesId;

  @JsonPOJOBuilder(withPrefix = "")
  public static class VisualAcuityBuilder {

  }
}
