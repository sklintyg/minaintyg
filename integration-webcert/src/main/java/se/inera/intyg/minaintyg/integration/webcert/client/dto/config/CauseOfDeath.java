package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CauseOfDeath.CauseOfDeathBuilder;

@JsonDeserialize(builder = CauseOfDeathBuilder.class)
@Value
@Builder
public class CauseOfDeath {

  String id;
  String descriptionId;
  String debutId;
  List<CodeItem> specifications;
  LocalDate maxDate;
  LocalDate minDate;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CauseOfDeathBuilder {

  }
}
