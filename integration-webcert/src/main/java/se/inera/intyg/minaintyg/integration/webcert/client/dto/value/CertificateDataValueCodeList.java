package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList.CertificateDataValueCodeListBuilder;

@JsonDeserialize(builder = CertificateDataValueCodeListBuilder.class)
@Value
@Builder
public class CertificateDataValueCodeList implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.CODE_LIST;
  List<CertificateDataValueCode> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueCodeListBuilder {

  }
}
