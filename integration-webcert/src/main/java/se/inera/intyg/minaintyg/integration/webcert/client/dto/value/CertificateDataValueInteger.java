package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueInteger.CertificateDataValueIntegerBuilder;

@JsonDeserialize(builder = CertificateDataValueIntegerBuilder.class)
@Value
@Builder
public class CertificateDataValueInteger implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.INTEGER;
  String id;
  Integer value;
  String unitOfMeasurement;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueIntegerBuilder {

  }
}
