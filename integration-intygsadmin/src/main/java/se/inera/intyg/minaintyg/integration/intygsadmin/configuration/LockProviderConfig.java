package se.inera.intyg.minaintyg.integration.intygsadmin.configuration;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;

@Configuration
public class LockProviderConfig {

  @Bean
  public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
    return new RedisLockProvider(connectionFactory, Application.MINA_INTYG.name());
  }
}
