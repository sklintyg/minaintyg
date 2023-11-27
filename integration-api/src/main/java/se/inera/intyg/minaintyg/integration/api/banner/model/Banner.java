package se.inera.intyg.minaintyg.integration.api.banner.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner.BannerBuilder;

@JsonDeserialize(builder = BannerBuilder.class)
@Value
@Builder
public class Banner {

  String id;

  String message;

  BannerPriority priority;

  @JsonPOJOBuilder(withPrefix = "")
  public static class BannerBuilder {

  }
}
