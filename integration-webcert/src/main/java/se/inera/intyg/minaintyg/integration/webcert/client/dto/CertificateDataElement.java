package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement.CertificateDataElementBuilder;


@JsonDeserialize(builder = CertificateDataElementBuilder.class)
@Value
@Builder
public class CertificateDataElement {

  private String id;
  private String parent;
  private int index;
  private CertificateDataConfig config;
  private CertificateDataValue value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateDataElementBuilder {

  }
}
