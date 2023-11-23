package se.inera.intyg.minaintyg.jobs;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.config.LockProviderConfig;
import se.inera.intyg.minaintyg.config.RedisConfig;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;

@Component
@RequiredArgsConstructor
public class BannerJob {

  private static final String JOB_NAME = "BannerJob.run";
  private final GetBannerIntegrationService getBannerIntegrationService;
  private final RedisCacheManager cacheManager;


  @Scheduled(cron = "${intygsadmin.cron}")
  @SchedulerLock(name = JOB_NAME, lockAtLeastFor = LockProviderConfig.LOCK_AT_LEAST, lockAtMostFor = LockProviderConfig.LOCK_AT_MOST)
  public void executeBannerJob() {
    final var banners = getBannerIntegrationService.get();
    Objects.requireNonNull(
        cacheManager.getCache(RedisConfig.BANNERS_CACHE)
    ).put(RedisConfig.BANNERS_CACHE_KEY, banners);
  }
}
