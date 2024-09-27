package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRecipient.CertificateRecipientBuilder;

@JsonDeserialize(builder = CertificateRecipientBuilder.class)
@Value
@Builder
public class CertificateRecipient {

  String id;
  String name;
  LocalDateTime sent;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateRecipientBuilder {

  }
}
