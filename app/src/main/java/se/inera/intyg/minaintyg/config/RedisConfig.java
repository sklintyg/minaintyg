/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RedisConfig {

  public static final String BANNERS_CACHE = "minaintyg:bannerCache";
  public static final String BANNERS_CACHE_KEY = "banners";

  @Bean
  public RedisCacheManager cacheManager(
      RedisConnectionFactory connectionFactory, JsonMapper jsonMapper) {
    return RedisCacheManager.builder(connectionFactory)
        .withCacheConfiguration(
            BANNERS_CACHE, redisCacheConfiguration(Duration.ofDays(1), jsonMapper))
        .build();
  }

  private RedisCacheConfiguration redisCacheConfiguration(
      Duration duration, JsonMapper jsonMapper) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(duration)
        .serializeValuesWith(serializationPair(jsonMapper));
  }

  private RedisSerializationContext.SerializationPair<Object> serializationPair(
      JsonMapper jsonMapper) {
    return RedisSerializationContext.SerializationPair.fromSerializer(
        new GenericJacksonJsonRedisSerializer(jsonMapper));
  }
}
