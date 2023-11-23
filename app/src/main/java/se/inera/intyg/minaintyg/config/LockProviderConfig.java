package se.inera.intyg.minaintyg.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class LockProviderConfig {

  private static final String MINA_INTYG = "minaintyg";

  public static final String LOCK_AT_MOST = "PT10M";
  public static final String LOCK_AT_LEAST = "PT30S";
  public static final String DEFAULT_LOCK_AT_MOST = "10m";

  @Bean
  public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
    return new RedisLockProvider(connectionFactory, MINA_INTYG);
  }
}
