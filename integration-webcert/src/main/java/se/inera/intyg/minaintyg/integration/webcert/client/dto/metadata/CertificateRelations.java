package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelations.CertificateRelationsBuilder;

@JsonDeserialize(builder = CertificateRelationsBuilder.class)
@Value
@Builder
public class CertificateRelations {

  private CertificateRelation parent;
  private CertificateRelation[] children;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateRelationsBuilder {

  }
}
