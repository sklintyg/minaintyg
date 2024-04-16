package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCauseOfDeath.CertificateDataValueCauseOfDeathBuilder;

@JsonDeserialize(builder = CertificateDataValueCauseOfDeathBuilder.class)
@Value
@Builder
public class CertificateDataValueCauseOfDeath implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.CAUSE_OF_DEATH;
  String id;
  CertificateDataValueDate debut;
  CertificateDataValueText description;
  CertificateDataValueCode specification;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueCauseOfDeathBuilder {

  }
}
