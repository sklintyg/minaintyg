package se.inera.intyg.minaintyg.integration.certificateanalyticsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.analytics.PublishAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage;
import se.inera.intyg.minaintyg.logging.MdcLogConstants;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishCertificateAnalyticsMessage implements PublishAnalyticsMessage {

  private final CertificateAnalyticsServiceProfile certificateAnalyticsServiceProfile;
  private final JmsTemplate jmsTemplateForCertificateAnalyticsMessages;

  @Override
  public void publishEvent(CertificateAnalyticsMessage message) {
    if (!certificateAnalyticsServiceProfile.isEnabled()) {
      log.debug("Certificate analytics service is not enabled - not publishing message");
      return;
    }

    jmsTemplateForCertificateAnalyticsMessages.convertAndSend(message, msg -> {
          msg.setStringProperty("messageId", message.getMessageId());
          msg.setStringProperty("sessionId", message.getEvent().getSessionId());
          msg.setStringProperty("traceId", MDC.get(MdcLogConstants.TRACE_ID_KEY));
          msg.setStringProperty("_type", message.getType());
          msg.setStringProperty("schemaVersion", message.getSchemaVersion());
          msg.setStringProperty("contentType", "application/json");
          msg.setStringProperty("messageType", message.getEvent().getMessageType().toString());
          return msg;
        }
    );
  }
}
