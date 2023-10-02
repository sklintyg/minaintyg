package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewTable.CertificateDataValueViewTableBuilder;

@JsonDeserialize(builder = CertificateDataValueViewTableBuilder.class)
@Value
@Builder
public class CertificateDataValueViewTable implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.VIEW_TABLE;
  List<CertificateDataValueViewRow> rows;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueViewTableBuilder {

  }
}
