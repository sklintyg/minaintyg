package se.inera.intyg.minaintyg.information.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBanner;

@Value
@Builder
public class InformationResponseDTO {

  List<FormattedBanner> banners;
}
