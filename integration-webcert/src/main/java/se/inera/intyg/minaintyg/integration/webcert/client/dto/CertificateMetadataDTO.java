package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateConfirmationModal;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelations;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateSummary;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Patient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Staff;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Unit;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateMetadataDTO {

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
}
