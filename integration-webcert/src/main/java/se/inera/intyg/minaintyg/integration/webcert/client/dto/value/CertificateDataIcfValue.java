package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataIcfValue.CertificateDataIcfValueBuilder.class)
@Value
@Builder
public class CertificateDataIcfValue implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.ICF;
  String id;
  String text;
  List<String> icfCodes;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataIcfValueBuilder {

  }
}
