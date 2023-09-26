package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadata.CertificateMetadataBuilder;

@Data
@Builder
@JsonDeserialize(builder = CertificateMetadataBuilder.class)
public class CertificateMetadata {


  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateMetadataBuilder {

  }
}
