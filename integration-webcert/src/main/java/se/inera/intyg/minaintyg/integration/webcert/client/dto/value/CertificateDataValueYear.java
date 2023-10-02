package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueYear.CertificateDataValueYearBuilder;

@JsonDeserialize(builder = CertificateDataValueYearBuilder.class)
@Value
@Builder
public class CertificateDataValueYear implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.YEAR;
  String id;
  Integer year;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueYearBuilder {

  }
}
