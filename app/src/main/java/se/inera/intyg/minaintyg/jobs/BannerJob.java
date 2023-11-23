package se.inera.intyg.minaintyg.jobs;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.config.LockProviderConfig;
import se.inera.intyg.minaintyg.information.service.BannerRepository;

@Component
@RequiredArgsConstructor
public class BannerJob {

  private static final String JOB_NAME = "BannerJob.run";
  private final BannerRepository bannerRepository;

  @Scheduled(cron = "${banner.job.interval}")
  @SchedulerLock(name = JOB_NAME, lockAtLeastFor = LockProviderConfig.LOCK_AT_LEAST, lockAtMostFor = LockProviderConfig.LOCK_AT_MOST)
  public void executeBannerJob() {
    bannerRepository.load();
  }
}
