package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRangeList.CertificateDataValueDateRangeListBuilder;

@JsonDeserialize(builder = CertificateDataValueDateRangeListBuilder.class)
@Value
@Builder
public class CertificateDataValueDateRangeList implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.DATE_RANGE_LIST;
  String id;
  List<CertificateDataValueDateRange> list;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueDateRangeListBuilder {

  }
}
