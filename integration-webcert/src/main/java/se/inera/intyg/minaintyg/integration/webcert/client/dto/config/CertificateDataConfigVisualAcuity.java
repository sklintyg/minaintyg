package se.inera.intyg.minaintyg.integration.webcert.client.dto.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigVisualAcuity.CertificateDataConfigVisualAcuityBuilder;

@JsonDeserialize(builder = CertificateDataConfigVisualAcuityBuilder.class)

@Value
@Builder
public class CertificateDataConfigVisualAcuity implements CertificateDataConfig {

  @Getter(onMethod = @__(@Override))
  CertificateDataConfigType type = CertificateDataConfigType.UE_VISUAL_ACUITY;
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
  String withoutCorrectionLabel;
  String withCorrectionLabel;
  String contactLensesLabel;
  VisualAcuity rightEye;
  VisualAcuity leftEye;
  VisualAcuity binocular;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataConfigVisualAcuityBuilder {

  }
}
