package se.inera.intyg.minaintyg.information.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormattedBanner {

  FormattedBannerType type;
  String content;
}
