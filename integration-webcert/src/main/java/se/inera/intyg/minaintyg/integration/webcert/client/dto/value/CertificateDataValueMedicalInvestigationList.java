package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueMedicalInvestigationList.CertificateDataValueMedicalInvestigationListBuilder;

@JsonDeserialize(builder = CertificateDataValueMedicalInvestigationListBuilder.class)
@Value
@Builder
public class CertificateDataValueMedicalInvestigationList implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.MEDICAL_INVESTIGATION_LIST;
  String id;
  List<CertificateDataValueMedicalInvestigation> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueMedicalInvestigationListBuilder {

  }
}
