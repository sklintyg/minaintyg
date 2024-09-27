package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelation.CertificateRelationBuilder;

@JsonDeserialize(builder = CertificateRelationBuilder.class)
@Value
@Builder
public class CertificateRelation {

  private String certificateId;
  private CertificateRelationType type;
  private CertificateStatus status;
  private LocalDateTime created;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateRelationBuilder {

  }
}
