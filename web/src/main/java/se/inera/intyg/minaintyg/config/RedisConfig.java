package se.inera.intyg.minaintyg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;

@Configuration
public class RedisConfig {

  @Value("${redis.host}")
  private String redisHost;
  @Value("${redis.port}")
  private String redisPort;
  @Value("${redis.password}")
  private String redisPassword;


  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    return standAloneConnectionFactory();
  }

  private JedisConnectionFactory standAloneConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(redisHost);
    redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
    if (StringUtils.hasLength(redisPassword)) {
      redisStandaloneConfiguration.setPassword(redisPassword);
    }
    return new JedisConnectionFactory(redisStandaloneConfiguration,
        JedisClientConfiguration.builder().usePooling().build());
  }
}
