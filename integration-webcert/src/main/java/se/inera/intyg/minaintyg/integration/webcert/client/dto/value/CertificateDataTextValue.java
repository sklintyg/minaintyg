package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataTextValue.CertificateDataTextValueBuilder.class)
@Value
@Builder
public class CertificateDataTextValue implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.TEXT;
  String id;
  String text;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataTextValueBuilder {

  }
}
