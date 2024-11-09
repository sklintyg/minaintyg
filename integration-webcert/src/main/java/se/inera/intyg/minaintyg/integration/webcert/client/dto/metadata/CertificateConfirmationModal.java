package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateConfirmationModal.CertificateConfirmationModalBuilder;

@JsonDeserialize(builder = CertificateConfirmationModalBuilder.class)
@Value
@Builder
public class CertificateConfirmationModal {

  String title;
  String text;
  Alert alert;
  String checkboxText;
  CertificateModalActionType primaryAction;
  CertificateModalActionType secondaryAction;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateConfirmationModalBuilder {

  }
}