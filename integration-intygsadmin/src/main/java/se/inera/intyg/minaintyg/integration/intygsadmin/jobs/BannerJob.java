package se.inera.intyg.minaintyg.integration.intygsadmin.jobs;

import static se.inera.intyg.minaintyg.integration.intygsadmin.configuration.RedisConfig.BANNERS;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.intygsadmin.IntygsadminBannerIntegrationService;

@Component
@RequiredArgsConstructor
public class BannerJob {

  private static final String JOB_NAME = "BannerJob.run";
  private static final String LOCK_AT_MOST = "PT10M";
  private static final String LOCK_AT_LEAST = "PT30S";
  private final IntygsadminBannerIntegrationService intygsadminBannerIntegrationService;
  private final RedisCacheManager cacheManager;


  @Scheduled(cron = "${intygsadmin.cron}")
  @SchedulerLock(name = JOB_NAME, lockAtLeastFor = LOCK_AT_LEAST, lockAtMostFor = LOCK_AT_MOST)
  public void executeBannerJob() {
    LockAssert.assertLocked();
    final var banners = intygsadminBannerIntegrationService.getActiveBannersFromIntygsadmin();
    Objects.requireNonNull(cacheManager.getCache(BANNERS))
        .put(Application.MINA_INTYG, banners);
  }
}
