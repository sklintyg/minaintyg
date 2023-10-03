package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewText.CertificateDataValueViewTextBuilder;

@JsonDeserialize(builder = CertificateDataValueViewTextBuilder.class)
@Value
@Builder
public class CertificateDataValueViewText implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.VIEW_TEXT;
  String text;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueViewTextBuilder {

  }
}
