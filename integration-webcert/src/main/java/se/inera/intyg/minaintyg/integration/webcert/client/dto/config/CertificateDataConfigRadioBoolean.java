package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean.CertificateDataConfigRadioBooleanBuilder;

@JsonDeserialize(builder = CertificateDataConfigRadioBooleanBuilder.class)
@Value
@Builder
public class CertificateDataConfigRadioBoolean implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_RADIO_BOOLEAN;
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
  String id;
  String selectedText;
  String unselectedText;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigRadioBooleanBuilder {

  }
}
