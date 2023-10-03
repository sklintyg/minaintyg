package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueVisualAcuities.CertificateDataValueVisualAcuitiesBuilder;

@JsonDeserialize(builder = CertificateDataValueVisualAcuitiesBuilder.class)
@Value
@Builder
public class CertificateDataValueVisualAcuities implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.VISUAL_ACUITIES;
  CertificateDataValueVisualAcuity rightEye;
  CertificateDataValueVisualAcuity leftEye;
  CertificateDataValueVisualAcuity binocular;


  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueVisualAcuitiesBuilder {

  }
}
