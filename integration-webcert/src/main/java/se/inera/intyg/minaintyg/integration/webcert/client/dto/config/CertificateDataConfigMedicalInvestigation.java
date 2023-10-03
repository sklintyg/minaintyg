package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigMedicalInvestigation.CertificateDataConfigMedicalInvestigationBuilder;

@JsonDeserialize(builder = CertificateDataConfigMedicalInvestigationBuilder.class)
@Value
@Builder
public class CertificateDataConfigMedicalInvestigation implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_MEDICAL_INVESTIGATION;
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
  String typeText;
  String dateText;
  String informationSourceText;
  String informationSourceDescription;
  List<MedicalInvestigation> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigMedicalInvestigationBuilder {

  }
}
