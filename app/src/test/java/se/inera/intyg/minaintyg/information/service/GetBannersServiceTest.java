package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBannersServiceTest {

  @InjectMocks
  private GetBannersService getBannersService;

  @Test
  void shouldReturnBanners() {
    final var response = getBannersService.get();

    assertEquals(Collections.emptyList(), response);
  }
}