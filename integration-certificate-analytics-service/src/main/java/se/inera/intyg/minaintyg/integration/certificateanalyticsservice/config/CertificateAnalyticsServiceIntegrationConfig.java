package se.inera.intyg.minaintyg.integration.certificateanalyticsservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class CertificateAnalyticsServiceIntegrationConfig {

  @Value("${certificate.analytics.message.queue.name}")
  private String queueName;

  @Bean
  public MappingJackson2MessageConverter messageConverter(ObjectMapper mapper) {
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    final var converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    converter.setObjectMapper(mapper);
    return converter;
  }

  @Bean
  public JmsTemplate jmsTemplateForCertificateAnalyticsMessages(ConnectionFactory connectionFactory,
      MappingJackson2MessageConverter converter) {
    final var jmsTemplate = new JmsTemplate(connectionFactory);
    jmsTemplate.setDefaultDestinationName(queueName);
    jmsTemplate.setMessageConverter(converter);
    jmsTemplate.setSessionTransacted(true);
    return jmsTemplate;
  }
}
