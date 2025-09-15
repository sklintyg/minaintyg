package se.inera.intyg.minaintyg.information.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
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
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@ExtendWith(MockitoExtension.class)
class DynamicLinksRepositoryTest {

  private DynamicLinkRepository repository;

  @Mock
  private Elva77LinkLoader elva77LinkLoader;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final String dummyJsonPath = "links/1177-navbar-services-dummy.json";

  @BeforeEach
  void setUp() throws Exception {
    final Resource dummyJson = new ClassPathResource(dummyJsonPath);
    Elva77MenuConfig dummyConfig = objectMapper.readValue(dummyJson.getInputStream(),
        Elva77MenuConfig.class);

    when(elva77LinkLoader.load(dummyJson, objectMapper)).thenReturn(dummyConfig);

    repository = new DynamicLinkRepository(objectMapper, elva77LinkLoader);
    ReflectionTestUtils.setField(repository, "resource", dummyJson);
    repository.init();
  }

  @Test
  void shouldReturnFullMenuConfig() {
    Elva77MenuConfig menuConfig = repository.get();
    assertNotNull(menuConfig);
    assertEquals("1.0.0-test", menuConfig.getVersion());
    assertEquals(3, menuConfig.getMenu().getItems().size());
  }

  @Test
  void shouldReturnFilteredLinksForGivenEnvironment() {
    List<DynamicLink> links = repository.get("prod");
    assertNotNull(links);
    assertEquals("https://prod.example.com/start",
        links.getFirst().getUrl().get("prod"));
  }

  @Test
  void shouldThrowExceptionWhenResourceIsInvalid() throws IOException {
    final Resource invalidResource = new ClassPathResource("links/nonexistent.json");
    when(elva77LinkLoader.load(invalidResource, objectMapper))
        .thenThrow(new RuntimeException("File not found"));

    DynamicLinkRepository faultyRepository = new DynamicLinkRepository(objectMapper,
        elva77LinkLoader);
    ReflectionTestUtils.setField(faultyRepository, "resource", invalidResource);

    try {
      faultyRepository.init();
    } catch (IllegalStateException e) {
      assertEquals("Failed to load dynamic links for environment: nonexistent.json",
          e.getMessage());
    }
  }
}
