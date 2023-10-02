package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCauseOfDeathList.CertificateDataConfigCauseOfDeathListBuilder;

@JsonDeserialize(builder = CertificateDataConfigCauseOfDeathListBuilder.class)
@Value
@Builder
public class CertificateDataConfigCauseOfDeathList implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_CAUSE_OF_DEATH_LIST;
  @Getter(onMethod = @__(@Override))
  String header;
  @Getter(onMethod = @__(@Override))
  String label;
  @Getter(onMethod = @__(@Override))
  String icon;
  @Getter(onMethod = @__(@Override))
  String text;
  @Getter(onMethod = @__(@Override))
  String description;
  @Getter(onMethod = @__(@Override))
  Accordion accordion;
  List<CauseOfDeath> list;


  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigCauseOfDeathListBuilder {

  }

}
