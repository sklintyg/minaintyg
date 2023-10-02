package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate.CertificateDataValueDateBuilder;

@JsonDeserialize(builder = CertificateDataValueDateBuilder.class)
@Value
@Builder
public class CertificateDataValueDate implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.DATE;
  String id;
  LocalDate date;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueDateBuilder {

  }
}
