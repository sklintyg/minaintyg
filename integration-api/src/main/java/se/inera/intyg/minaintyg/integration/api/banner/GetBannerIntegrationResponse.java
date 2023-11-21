package se.inera.intyg.minaintyg.integration.api.banner;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;

@Value
@Builder
public class GetBannerIntegrationResponse {

  @Builder.Default()
  List<Banner> banners = Collections.emptyList();
}
