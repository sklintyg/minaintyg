package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCauseOfDeathList.CertificateDataValueCauseOfDeathListBuilder;

@JsonDeserialize(builder = CertificateDataValueCauseOfDeathListBuilder.class)
@Value
@Builder
public class CertificateDataValueCauseOfDeathList implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.CAUSE_OF_DEATH_LIST;
  List<CertificateDataValueCauseOfDeath> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueCauseOfDeathListBuilder {

  }
}
