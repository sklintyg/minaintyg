package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMessageType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateMetadata.CertificateMetadataBuilder;

@JsonDeserialize(builder = CertificateMetadataBuilder.class)
@Data
@Builder
public class CertificateMetadata {

  private String id;
  private String type;
  private String typeVersion;
  private String typeName;
  private String name;
  private String description;
  private LocalDateTime created;
  private CertificateStatus status;
  private boolean testCertificate;
  private boolean forwarded;
  private boolean sent;
  private boolean availableForCitizen;
  private String sentTo;
  private CertificateRelations relations;
  private Unit unit;
  private Unit careUnit;
  private Unit careProvider;
  private Patient patient;
  private Staff issuedBy;
  private long version;
  private boolean latestMajorVersion;
  private LocalDateTime readyForSign;
  private LocalDateTime signed;
  private LocalDateTime modified;
  private String responsibleHospName;
  private CertificateRecipient recipient;
  private CertificateSummary summary;
  private CertificateConfirmationModal confirmationModal;
  private boolean validForSign;
  private String externalReference;
  private List<CertificateMessageType> messageTypes;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateMetadataBuilder {

  }
}
