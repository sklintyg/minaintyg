package se.inera.intyg.minaintyg.integration.intygsadmin;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@Component
public class BannerConverter {

  public List<Banner> convert(BannerDTO[] banners) {
    return Collections.emptyList();
  }
}
