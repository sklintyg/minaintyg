package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList.CertificateDataValueDateListBuilder;

@JsonDeserialize(builder = CertificateDataValueDateListBuilder.class)
@Value
@Builder
public class CertificateDataValueDateList implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.DATE_LIST;
  List<CertificateDataValueDate> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueDateListBuilder {

  }
}
