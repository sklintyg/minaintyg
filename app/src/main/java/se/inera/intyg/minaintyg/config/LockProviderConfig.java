package se.inera.intyg.minaintyg.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class LockProviderConfig {

  private static final String INTYG = "intyg";

  public static final String LOCK_AT_MOST = "PT10M";
  public static final String LOCK_AT_LEAST = "PT30S";

  @Bean
  public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
    return new RedisLockProvider(connectionFactory, INTYG);
  }
}
