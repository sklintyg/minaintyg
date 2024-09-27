package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate.CertificateDataConfigCheckboxMultipleDateBuilder;

@JsonDeserialize(builder = CertificateDataConfigCheckboxMultipleDateBuilder.class)
@Value
@Builder
public class CertificateDataConfigCheckboxMultipleDate implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigType type = CertificateDataConfigType.UE_CHECKBOX_MULTIPLE_DATE;
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
  Layout layout;
  List<CheckboxMultipleDate> list;


  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigCheckboxMultipleDateBuilder {

  }
}

