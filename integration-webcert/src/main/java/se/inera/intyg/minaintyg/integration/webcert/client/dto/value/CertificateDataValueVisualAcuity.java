package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueVisualAcuity.CertificateDataValueVisualAcuityBuilder;

@JsonDeserialize(builder = CertificateDataValueVisualAcuityBuilder.class)
@Value
@Builder
public class CertificateDataValueVisualAcuity implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.VISUAL_ACUITY;
  CertificateDataValueDouble withoutCorrection;
  CertificateDataValueDouble withCorrection;
  CertificateDataValueBoolean contactLenses;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueVisualAcuityBuilder {

  }
}
