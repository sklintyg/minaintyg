package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigViewList.CertificateDataConfigViewListBuilder;

@JsonDeserialize(builder = CertificateDataConfigViewListBuilder.class)
@Value
@Builder
public class CertificateDataConfigViewList implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_VIEW_LIST;
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
  public static class CertificateDataConfigViewListBuilder {

  }
}
