package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateSummary.CertificateSummaryBuilder;

@JsonDeserialize(builder = CertificateSummaryBuilder.class)
@Value
@Builder
public class CertificateSummary {

  String label;
  String value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateSummaryBuilder {

  }
}