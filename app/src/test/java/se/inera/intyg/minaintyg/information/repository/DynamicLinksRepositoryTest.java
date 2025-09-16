package se.inera.intyg.minaintyg.information.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.minaintyg.information.service.DynamicLinkRepository;
import se.inera.intyg.minaintyg.information.service.Elva77LinkLoader;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;
import se.inera.intyg.minaintyg.information.service.model.Elva77Menu;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuLinks;

@ExtendWith(MockitoExtension.class)
class DynamicLinksRepositoryTest {

  private DynamicLinkRepository repository;

  public static final Elva77MenuLinks LINK = Elva77MenuLinks.builder()
      .id("1")
      .name("test")
      .url(Map.of("prod", "https://prod.example.com/start"))
      .isAgentModeSupported(false)
      .build();

  public static final Elva77Menu MENU = Elva77Menu.builder()
      .items(List.of(LINK))
      .build();

  public static final Elva77MenuConfig MENU_CONFIG = Elva77MenuConfig.builder()
      .menu(MENU)
      .build();

  final String dummyJsonPath = "links/1177-navbar-services-dummy.json";
  final Resource dummyJson = new ClassPathResource(dummyJsonPath);

  @Mock
  private Elva77LinkLoader elva77LinkLoader;

  @BeforeEach
  void setUp() {
    repository = new DynamicLinkRepository(elva77LinkLoader);
    ReflectionTestUtils.setField(repository, "resource", dummyJson);
    ReflectionTestUtils.setField(repository, "environmentType", "prod");
    ReflectionTestUtils.setField(repository, "menuSettingUrl", "https://st.sob.1177.se/");
    ReflectionTestUtils.setField(repository, "settingName", "Inställningar");
    ReflectionTestUtils.setField(repository, "settingId", "99");
  }

  @Test
  void shouldReturnExpectedLinks() {
    when(elva77LinkLoader.load(dummyJson)).thenReturn(MENU_CONFIG);

    repository.init();

    final var expected = DynamicLink.builder()
        .id(LINK.getId())
        .name(LINK.getName())
        .url(LINK.getUrl().get("prod"))
        .build();
    final var actual = repository.get().getFirst();

    assertEquals(expected, actual);
  }

  @Test
  void shouldAppendSettingsLink() {
    when(elva77LinkLoader.load(dummyJson)).thenReturn(MENU_CONFIG);

    repository.init();

    final var expected = DynamicLink.builder()
        .id("99")
        .name("Inställningar")
        .url("https://st.sob.1177.se/")
        .build();

    final var actual = repository.get().getLast();

    assertEquals(expected, actual);
  }
}
