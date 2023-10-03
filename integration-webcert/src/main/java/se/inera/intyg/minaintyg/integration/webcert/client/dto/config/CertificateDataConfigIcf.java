package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigIcf.CertificateDataConfigIcfBuilder;

@JsonDeserialize(builder = CertificateDataConfigIcfBuilder.class)
@Value
@Builder
public class CertificateDataConfigIcf implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_ICF;
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
  String modalLabel;
  String collectionsLabel;
  String placeholder;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigIcfBuilder {

  }
}
