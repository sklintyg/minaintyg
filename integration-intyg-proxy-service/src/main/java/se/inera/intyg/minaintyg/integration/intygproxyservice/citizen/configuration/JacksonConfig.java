package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;

@Slf4j
@Configuration
public class JacksonConfig {
  @Bean
  public JsonMapperBuilderCustomizer jacksonCustomizer() {
    log.info("Jackson Customizer");
    return builder -> builder.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
  }
}
