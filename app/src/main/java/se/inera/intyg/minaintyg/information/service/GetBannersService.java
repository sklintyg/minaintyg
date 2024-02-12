package se.inera.intyg.minaintyg.information.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.dto.FormattedBanner;

@Service
@RequiredArgsConstructor
public class GetBannersService {

  private final BannerRepository bannerRepository;
  private final FormattedBannerConverter formattedBannerConverter;

  public List<FormattedBanner> get() {
    final var response = bannerRepository.get();

    return response.getBanners()
        .stream()
        .map(formattedBannerConverter::convert)
        .toList();
  }
}
