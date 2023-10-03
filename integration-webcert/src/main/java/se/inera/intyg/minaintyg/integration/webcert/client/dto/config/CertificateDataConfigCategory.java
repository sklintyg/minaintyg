package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory.CertificateDataConfigCategoryBuilder;

@JsonDeserialize(builder = CertificateDataConfigCategoryBuilder.class)
@Value
@Builder
public class CertificateDataConfigCategory implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigTypes type = CertificateDataConfigTypes.CATEGORY;
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

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigCategoryBuilder {

  }
}
