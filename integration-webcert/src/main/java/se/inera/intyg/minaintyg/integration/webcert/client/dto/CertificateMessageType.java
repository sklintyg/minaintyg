package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMessageType.CertificateMessageTypeBuilder;

@JsonDeserialize(builder = CertificateMessageTypeBuilder.class)
@Value
@Builder
public class CertificateMessageType {

  MessageType type;
  String subject;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateMessageTypeBuilder {

  }
}
