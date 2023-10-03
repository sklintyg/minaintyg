package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.MedicalInvestigation.MedicalInvestigationBuilder;

@JsonDeserialize(builder = MedicalInvestigationBuilder.class)
@Value
@Builder
public class MedicalInvestigation {

  String investigationTypeId;
  String informationSourceId;
  String dateId;
  List<CodeItem> typeOptions;
  LocalDate maxDate;
  LocalDate minDate;

  @JsonPOJOBuilder(withPrefix = "")
  public static class MedicalInvestigationBuilder {

  }
}
