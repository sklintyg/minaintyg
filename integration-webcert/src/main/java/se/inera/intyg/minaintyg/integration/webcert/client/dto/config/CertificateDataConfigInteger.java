package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigInteger.CertificateDataConfigIntegerBuilder;

@JsonDeserialize(builder = CertificateDataConfigIntegerBuilder.class)
@Value
@Builder
public class CertificateDataConfigInteger implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigType type = CertificateDataConfigType.UE_INTEGER;
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
  @Getter(onMethod = @__(@Override))
  Message message;
  String id;
  String unitOfMeasurement;
  Integer min;
  Integer max;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigIntegerBuilder {

  }
}
