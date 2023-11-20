package se.inera.intyg.minaintyg.integration.api.banner;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;

@Value
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBannerIntegrationResponse {

  @Builder.Default()
  List<Banner> banners = Collections.emptyList();
}
