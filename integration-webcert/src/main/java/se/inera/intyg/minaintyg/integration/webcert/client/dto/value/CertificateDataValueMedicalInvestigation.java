package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueMedicalInvestigation.CertificateDataValueMedicalInvestigationBuilder;


@JsonDeserialize(builder = CertificateDataValueMedicalInvestigationBuilder.class)
@Value
@Builder
public class CertificateDataValueMedicalInvestigation implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.MEDICAL_INVESTIGATION;
  CertificateDataValueDate date;
  CertificateDataTextValue informationSource;
  CertificateDataValueCode investigationType;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueMedicalInvestigationBuilder {

  }
}
