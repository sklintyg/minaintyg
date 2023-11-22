package se.inera.intyg.minaintyg.integration.intygsadmin.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.intygsadmin.IntygsadminBannerIntegrationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class BannerJob {

  private static final String JOB_NAME = "BannerJob.run";
  private static final String LOCK_AT_MOST = "PT10M";
  private static final String LOCK_AT_LEAST = "PT30S";
  private final IntygsadminBannerIntegrationService intygsadminBannerIntegrationService;


  @Scheduled(cron = "${intygsadmin.cron}")
  @SchedulerLock(name = JOB_NAME, lockAtLeastFor = LOCK_AT_LEAST, lockAtMostFor = LOCK_AT_MOST)
  public void executeBannerJob() {
    LockAssert.assertLocked();
    final var getBannerIntegrationResponse = intygsadminBannerIntegrationService.getBannersFromIntygsadmin(
        Application.MINA_INTYG
    );
    log.info("Banners from IA '{}'", (Object) getBannerIntegrationResponse);
  }
}
