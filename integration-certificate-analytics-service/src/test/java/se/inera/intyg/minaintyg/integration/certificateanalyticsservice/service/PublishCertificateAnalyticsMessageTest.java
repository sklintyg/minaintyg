package se.inera.intyg.minaintyg.integration.certificateanalyticsservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.jms.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import se.inera.intyg.minaintyg.integration.api.analytics.model.AnalyticsCertificate;
import se.inera.intyg.minaintyg.integration.api.analytics.model.AnalyticsEvent;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessageType;
import se.inera.intyg.minaintyg.logging.MdcLogConstants;

@ExtendWith(MockitoExtension.class)
class PublishCertificateAnalyticsMessageTest {

  @Mock
  private CertificateAnalyticsServiceProfile certificateAnalyticsServiceProfile;

  @Mock
  private JmsTemplate jmsTemplateForCertificateAnalyticsMessages;

  @InjectMocks
  private PublishCertificateAnalyticsMessage publishCertificateAnalyticsMessage;

  @Test
  void shallPublishMessageToJmsQueue() {
    final var expected = CertificateAnalyticsMessage.builder()
        .certificate(
            AnalyticsCertificate.builder()
                .id("test")
                .build()
        )
        .build();

    when(certificateAnalyticsServiceProfile.isEnabled()).thenReturn(true);

    final var captor = ArgumentCaptor.forClass(CertificateAnalyticsMessage.class);

    doNothing().when(jmsTemplateForCertificateAnalyticsMessages)
        .convertAndSend(captor.capture(), any());

    publishCertificateAnalyticsMessage.publishEvent(expected);

    assertEquals(expected, captor.getValue());
  }

  @Test
  void shallNotPublishMessagesIfCertificateAnalyticsNotActive() {
    when(certificateAnalyticsServiceProfile.isEnabled()).thenReturn(false);

    final var message = CertificateAnalyticsMessage.builder()
        .certificate(
            AnalyticsCertificate.builder()
                .id("test")
                .build()
        )
        .build();

    publishCertificateAnalyticsMessage.publishEvent(message);

    verifyNoInteractions(jmsTemplateForCertificateAnalyticsMessages);
  }

  @Nested
  class MessagePropertyTests {

    @Captor
    private ArgumentCaptor<MessagePostProcessor> mppCaptor;

    private final String sessionId = "sess-456";
    private final String traceId = "trace-789";
    private final CertificateAnalyticsMessage message = CertificateAnalyticsMessage.builder()
        .certificate(
            AnalyticsCertificate.builder()
                .id("test")
                .build()
        )
        .event(
            AnalyticsEvent.builder()
                .messageType(CertificateAnalyticsMessageType.CERTIFICATE_PRINTED_BY_CITIZEN)
                .sessionId(sessionId)
                .build()
        )
        .build();

    @BeforeEach
    void setUp() {
      MDC.put(MdcLogConstants.TRACE_ID_KEY, traceId);

      when(certificateAnalyticsServiceProfile.isEnabled()).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
      MDC.clear();
    }

    @Test
    void shallSetMessageIdPropertyWhenPublishing() throws Exception {
      publishCertificateAnalyticsMessage.publishEvent(message);

      verify(jmsTemplateForCertificateAnalyticsMessages).convertAndSend(eq(message),
          mppCaptor.capture());

      final var mpp = mppCaptor.getValue();
      final var jmsMsg = mock(Message.class);
      mpp.postProcessMessage(jmsMsg);

      verify(jmsMsg).setStringProperty("messageId", message.getMessageId());
    }

    @Test
    void shallSetSessionIdPropertyWhenPublishing() throws Exception {
      publishCertificateAnalyticsMessage.publishEvent(message);

      verify(jmsTemplateForCertificateAnalyticsMessages).convertAndSend(eq(message),
          mppCaptor.capture());

      final var mpp = mppCaptor.getValue();
      final var jmsMsg = mock(Message.class);
      mpp.postProcessMessage(jmsMsg);

      verify(jmsMsg).setStringProperty("sessionId", sessionId);
    }

    @Test
    void shallSetTraceIdPropertyWhenPublishing() throws Exception {
      publishCertificateAnalyticsMessage.publishEvent(message);

      verify(jmsTemplateForCertificateAnalyticsMessages).convertAndSend(eq(message),
          mppCaptor.capture());

      final var mpp = mppCaptor.getValue();
      final var jmsMsg = mock(Message.class);
      mpp.postProcessMessage(jmsMsg);

      verify(jmsMsg).setStringProperty("traceId", traceId);
    }

    @Test
    void shallSetTypePropertyWhenPublishing() throws Exception {
      publishCertificateAnalyticsMessage.publishEvent(message);

      verify(jmsTemplateForCertificateAnalyticsMessages).convertAndSend(eq(message),
          mppCaptor.capture());

      final var mpp = mppCaptor.getValue();
      final var jmsMsg = mock(Message.class);
      mpp.postProcessMessage(jmsMsg);

      verify(jmsMsg).setStringProperty("_type", message.getType());
    }

    @Test
    void shallSetSchemaVersionPropertyWhenPublishing() throws Exception {
      publishCertificateAnalyticsMessage.publishEvent(message);

      verify(jmsTemplateForCertificateAnalyticsMessages).convertAndSend(eq(message),
          mppCaptor.capture());

      final var mpp = mppCaptor.getValue();
      final var jmsMsg = mock(Message.class);
      mpp.postProcessMessage(jmsMsg);

      verify(jmsMsg).setStringProperty("schemaVersion", message.getSchemaVersion());
    }

    @Test
    void shallSetContentTypePropertyWhenPublishing() throws Exception {
      publishCertificateAnalyticsMessage.publishEvent(message);

      verify(jmsTemplateForCertificateAnalyticsMessages).convertAndSend(eq(message),
          mppCaptor.capture());

      final var mpp = mppCaptor.getValue();
      final var jmsMsg = mock(Message.class);
      mpp.postProcessMessage(jmsMsg);

      verify(jmsMsg).setStringProperty("contentType", "application/json");
    }

    @Test
    void shallSetMessageTypePropertyWhenPublishing() throws Exception {
      publishCertificateAnalyticsMessage.publishEvent(message);

      verify(jmsTemplateForCertificateAnalyticsMessages).convertAndSend(eq(message),
          mppCaptor.capture());

      final var mpp = mppCaptor.getValue();
      final var jmsMsg = mock(Message.class);
      mpp.postProcessMessage(jmsMsg);

      verify(jmsMsg).setStringProperty("messageType",
          message.getEvent().getMessageType().toString());
    }
  }
}