package se.inera.intyg.minaintyg.integration.certificateanalyticsservice.service;

import static se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessageType.CERTIFICATE_PRINTED_BY_CITIZEN;
import static se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessageType.CERTIFICATE_PRINTED_CUSTOMIZED_BY_CITIZEN;
import static se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessageType.CERTIFICATE_SENT_BY_CITIZEN;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.analytics.AnalyticsMessageFactory;
import se.inera.intyg.minaintyg.integration.api.analytics.model.AnalyticsCertificate;
import se.inera.intyg.minaintyg.integration.api.analytics.model.AnalyticsEvent;
import se.inera.intyg.minaintyg.integration.api.analytics.model.AnalyticsRecipient;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage.CertificateAnalyticsMessageBuilder;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessageType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.user.LoggedInMinaIntygUserService;
import se.inera.intyg.minaintyg.logging.MdcLogConstants;

@Component
@RequiredArgsConstructor
public class CertificateAnalyticsMessageFactory implements AnalyticsMessageFactory {

  private final LoggedInMinaIntygUserService loggedInMinaIntygUserService;

  @Override
  public CertificateAnalyticsMessage certificatePrinted(Certificate certificate) {
    return create(certificate, CERTIFICATE_PRINTED_BY_CITIZEN).build();
  }

  @Override
  public CertificateAnalyticsMessage certificatePrintedCustomized(Certificate certificate) {
    return create(certificate, CERTIFICATE_PRINTED_CUSTOMIZED_BY_CITIZEN).build();
  }

  @Override
  public CertificateAnalyticsMessage certificateSent(Certificate certificate, String recipient) {
    return create(certificate, CERTIFICATE_SENT_BY_CITIZEN)
        .recipient(
            AnalyticsRecipient.builder()
                .id(recipient)
                .build()
        )
        .build();
  }

  private CertificateAnalyticsMessageBuilder create(Certificate certificate,
      CertificateAnalyticsMessageType type) {
    final var loggedInMinaIntygUser = loggedInMinaIntygUserService.loggedInMinaIntygUser();
    return CertificateAnalyticsMessage.builder()
        .event(
            AnalyticsEvent.builder()
                .timestamp(LocalDateTime.now())
                .messageType(type)
                .userId(loggedInMinaIntygUser.getPersonId())
                .sessionId(MDC.get(MdcLogConstants.SESSION_ID_KEY))
                .build()
        )
        .certificate(
            AnalyticsCertificate.builder()
                .id(certificate.getMetadata().getId())
                .type(certificate.getMetadata().getType().getId())
                .typeVersion(certificate.getMetadata().getType().getVersion())
                .patientId(loggedInMinaIntygUser.getPersonId())
                .unitId(certificate.getMetadata().getUnit().getId())
                .careProviderId(certificate.getMetadata().getCareProvider().getId())
                .build()
        );
  }
}
