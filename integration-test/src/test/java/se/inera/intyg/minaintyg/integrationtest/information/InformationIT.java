package se.inera.intyg.minaintyg.integrationtest.information;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.minaintyg.integrationtest.environment.IntygProxyServiceMock.ATHENA_REACT_ANDERSSON;
import static se.inera.intyg.minaintyg.integrationtest.util.BannerUtil.activeBanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import se.inera.intyg.minaintyg.information.dto.FormattedBanner;
import se.inera.intyg.minaintyg.information.dto.FormattedBannerType;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerPriorityDTO;
import se.inera.intyg.minaintyg.integrationtest.environment.Containers;
import se.inera.intyg.minaintyg.integrationtest.environment.IntygProxyServiceMock;
import se.inera.intyg.minaintyg.integrationtest.environment.IntygsadminMock;
import se.inera.intyg.minaintyg.integrationtest.util.ApiUtil;

@ActiveProfiles({"integration-test", "testability"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class InformationIT {

  @LocalServerPort
  private int port;

  private final TestRestTemplate restTemplate;
  private ApiUtil api;
  private MockServerClient mockServerClient;
  private IntygProxyServiceMock intygProxyServiceMock;
  private IntygsadminMock intygsadminMock;

  @Autowired
  public InformationIT(TestRestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @BeforeAll
  public static void beforeAll() {
    Containers.ensureRunning();
  }

  @BeforeEach
  void setUp() {
    this.api = new ApiUtil(restTemplate, port);
    this.mockServerClient = new MockServerClient(
        Containers.MOCK_SERVER_CONTAINER.getHost(),
        Containers.MOCK_SERVER_CONTAINER.getServerPort()
    );
    this.intygProxyServiceMock = new IntygProxyServiceMock(mockServerClient);
    this.intygsadminMock = new IntygsadminMock(mockServerClient);
  }

  @AfterEach
  void tearDown() throws Exception {
    api.testabilityFakeLogout();
    mockServerClient.reset();
    Containers.REDIS_CONTAINER.execInContainer("redis-cli", "flushall");
  }

  @Test
  void shallReturn403IfNotAuthenticatedUser() {
    final var response = api.information();

    assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
  }

  @Test
  void shallReturnEmptyBannersIfNoBannersReturned() {
    intygProxyServiceMock.foundPerson(ATHENA_REACT_ANDERSSON);
    intygsadminMock.emptyBanners();

    api.testabilityFakeLogin(ATHENA_REACT_ANDERSSON.getPersonnummer());

    final var response = api.information();

    assertTrue(response.getBody().getBanners().isEmpty(),
        "Should not contain any banners when none active: %s".formatted(response.getBody())
    );
  }

  @Test
  void shallReturnActiveBanner() {
    intygProxyServiceMock.foundPerson(ATHENA_REACT_ANDERSSON);
    intygsadminMock.foundBanners(
        activeBanner(BannerPriorityDTO.HOG, "This is a message with high priority")
    );
    api.testabilityFakeLogin(ATHENA_REACT_ANDERSSON.getPersonnummer());

    final var expectedBanner = FormattedBanner.builder()
        .type(FormattedBannerType.ERROR)
        .content("This is a message with high priority")
        .build();

    final var response = api.information();

    assertTrue(response.getBody().getBanners().contains(expectedBanner),
        "Expected banner %s but received %s".formatted(expectedBanner, response.getBody())
    );
  }

  @Test
  void shallReturnMenuLinks() {
    intygProxyServiceMock.foundPerson(ATHENA_REACT_ANDERSSON);
    intygsadminMock.emptyBanners();

    api.testabilityFakeLogin(ATHENA_REACT_ANDERSSON.getPersonnummer());

    final var response = api.information();

    assertFalse(response.getBody().getLinks().isEmpty(), "should contain menu links");
  }
}
