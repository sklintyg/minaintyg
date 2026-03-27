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
  @SchedulerLock(
      name = JOB_NAME,
      lockAtLeastFor = LockProviderConfig.LOCK_AT_LEAST,
      lockAtMostFor = LockProviderConfig.LOCK_AT_MOST)
  public void executeBannerJob() {
    bannerRepository.load();
  }
}
