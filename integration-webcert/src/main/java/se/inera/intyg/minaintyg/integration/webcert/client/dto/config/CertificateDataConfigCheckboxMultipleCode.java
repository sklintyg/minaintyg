package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleCode.CertificateDataConfigCheckboxMultipleCodeBuilder;

@JsonDeserialize(builder = CertificateDataConfigCheckboxMultipleCodeBuilder.class)
@Value
@Builder
public class CertificateDataConfigCheckboxMultipleCode implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_CHECKBOX_MULTIPLE_CODE;
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
  Layout layout;
  List<CheckboxMultipleCode> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigCheckboxMultipleCodeBuilder {

  }
}

