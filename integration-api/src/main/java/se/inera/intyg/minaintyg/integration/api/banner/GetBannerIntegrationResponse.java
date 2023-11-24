package se.inera.intyg.minaintyg.integration.api.banner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse.GetBannerIntegrationResponseBuilder;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;

@JsonDeserialize(builder = GetBannerIntegrationResponseBuilder.class)
@Value
@Builder
public class GetBannerIntegrationResponse {

  @Builder.Default()
  List<Banner> banners = Collections.emptyList();

  @JsonPOJOBuilder(withPrefix = "")
  public static class GetBannerIntegrationResponseBuilder {

  }
}
