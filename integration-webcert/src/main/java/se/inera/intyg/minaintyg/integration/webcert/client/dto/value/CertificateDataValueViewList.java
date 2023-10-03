package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewList.CertificateDataValueViewListBuilder;

@JsonDeserialize(builder = CertificateDataValueViewListBuilder.class)
@Value
@Builder
public class CertificateDataValueViewList implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.VIEW_LIST;
  List<CertificateDataValueViewText> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueViewListBuilder {

  }
}
