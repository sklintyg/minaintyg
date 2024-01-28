package se.inera.intyg.minaintyg.information.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InformationResponseDTO {

  List<FormattedBanner> banners;
}
