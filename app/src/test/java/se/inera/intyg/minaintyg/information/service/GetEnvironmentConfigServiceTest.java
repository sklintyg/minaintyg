package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.minaintyg.information.dto.DynamicLink;
import se.inera.intyg.minaintyg.information.dto.FormattedDynamicLink;

@ExtendWith(MockitoExtension.class)
class GetEnvironmentConfigServiceTest {


  @Mock
  private EnvironmentConfigRepository environmentConfigRepository;

  private GetEnvironmentConfigService getEnvironmentConfigService;

  private static final FormattedDynamicLink EXPECTED_LINK = FormattedDynamicLink.builder()
      .id("1")
      .name("test")
      .url("test.url")
      .build();

  @BeforeEach
  void setup() {
    getEnvironmentConfigService = new GetEnvironmentConfigService(
        environmentConfigRepository);
  }

  @Test
  void shouldReturnExpectedLinksWhenEnvironmentIsSetToProduction() {

    Map<String, DynamicLink> mockLinks = Map.of(
        "1", DynamicLink.builder()
            .id("1")
            .name("test")
            .url(Map.of("prod", "test.url"))
            .build()
    );
    when(environmentConfigRepository.get()).thenReturn(mockLinks);

    ReflectionTestUtils.setField(getEnvironmentConfigService, "environmentType", "prod");

    Map<String, FormattedDynamicLink> result = getEnvironmentConfigService.get();

    assertEquals(1, result.size());
    assertEquals(EXPECTED_LINK, result.get("1"));
  }
}
