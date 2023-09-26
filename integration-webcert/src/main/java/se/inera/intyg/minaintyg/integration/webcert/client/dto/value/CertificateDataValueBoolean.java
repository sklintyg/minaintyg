package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean.CertificateDataValueBooleanBuilder;

@JsonDeserialize(builder = CertificateDataValueBooleanBuilder.class)
@Value
@Builder
public class CertificateDataValueBoolean implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.BOOLEAN;
  String id;
  Boolean selected;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueBooleanBuilder {

  }
}
