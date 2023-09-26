package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewRow.CertificateDataValueViewRowBuilder;

@JsonDeserialize(builder = CertificateDataValueViewRowBuilder.class)
@Value
@Builder
public class CertificateDataValueViewRow implements CertificateDataValue {

  @Getter(onMethod = @__(@Override))
  CertificateDataValueType type = CertificateDataValueType.VIEW_ROW;
  List<CertificateDataTextValue> columns;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataValueViewRowBuilder {

  }
}
