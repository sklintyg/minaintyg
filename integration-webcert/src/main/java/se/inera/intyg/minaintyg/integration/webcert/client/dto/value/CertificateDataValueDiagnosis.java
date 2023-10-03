package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis.CertificateDataValueDiagnosisBuilder;

@JsonDeserialize(builder = CertificateDataValueDiagnosisBuilder.class)
@Value
@Builder
public class CertificateDataValueDiagnosis implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.DIAGNOSIS;
  String id;
  String terminology;
  String code;
  String description;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueDiagnosisBuilder {

  }
}
