package se.inera.intyg.minaintyg.jobs;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.information.service.BannerRepository;

@ExtendWith(MockitoExtension.class)
class BannerJobTest {

  @Mock
  private BannerRepository bannerRepository;
  @InjectMocks
  private BannerJob bannerJob;

  @Test
  void shouldUpdateCache() {
    bannerJob.executeBannerJob();
    verify(bannerRepository).load();
  }
}
