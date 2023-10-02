package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList.CertificateDataValueDiagnosisListBuilder;

@JsonDeserialize(builder = CertificateDataValueDiagnosisListBuilder.class)
@Value
@Builder
public class CertificateDataValueDiagnosisList implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.DIAGNOSIS_LIST;
  List<CertificateDataValueDiagnosis> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueDiagnosisListBuilder {

  }
}
