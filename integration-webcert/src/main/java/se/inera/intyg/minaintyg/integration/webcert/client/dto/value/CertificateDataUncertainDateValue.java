package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataUncertainDateValue.CertificateDataUncertainDateValueBuilder.class)
@Value
@Builder
public class CertificateDataUncertainDateValue implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.UNCERTAIN_DATE;
  String id;
  String value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataUncertainDateValueBuilder {

  }
}
