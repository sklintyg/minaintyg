package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement.CertificateDataElementBuilder;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;


@JsonDeserialize(builder = CertificateDataElementBuilder.class)
@Value
@Builder
public class CertificateDataElement {

  String id;
  String parent;
  int index;
  CertificateDataConfig config;
  CertificateDataValue value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataElementBuilder {

  }
}
