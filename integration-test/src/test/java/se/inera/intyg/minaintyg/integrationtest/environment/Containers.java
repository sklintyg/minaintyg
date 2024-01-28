package se.inera.intyg.minaintyg.integrationtest.environment;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class Containers {

  public static MockServerContainer MOCK_SERVER_CONTAINER;
  public static GenericContainer<?> REDIS_CONTAINER;

  public static void ensureRunning() {
    mockServerContainer();
    redisContainer();
  }

  private static void mockServerContainer() {
    if (MOCK_SERVER_CONTAINER == null) {
      MOCK_SERVER_CONTAINER = new MockServerContainer(
          DockerImageName.parse("mockserver/mockserver:5.15.0")
      );
    }

    if (!MOCK_SERVER_CONTAINER.isRunning()) {
      MOCK_SERVER_CONTAINER.start();
    }

    final var mockServerContainerHost = MOCK_SERVER_CONTAINER.getHost();
    final var mockServerContainerPort = String.valueOf(MOCK_SERVER_CONTAINER.getServerPort());

    System.setProperty("integration.intygproxyservice.baseurl", mockServerContainerHost);
    System.setProperty("integration.intygproxyservice.port", mockServerContainerPort);
    System.setProperty("integration.intygsadmin.baseurl", mockServerContainerHost);
    System.setProperty("integration.intygsadmin.port", mockServerContainerPort);
    System.setProperty("integration.intygstjanst.baseurl", mockServerContainerHost);
    System.setProperty("integration.intygstjanst.port", mockServerContainerPort);
    System.setProperty("integration.webcert.baseurl", mockServerContainerHost);
    System.setProperty("integration.webcert.port", mockServerContainerPort);
  }

  private static void redisContainer() {
    if (REDIS_CONTAINER == null) {
      REDIS_CONTAINER = new GenericContainer<>(
          DockerImageName.parse("redis:6.0.9-alpine")
      ).withExposedPorts(6379);
    }

    if (!REDIS_CONTAINER.isRunning()) {
      REDIS_CONTAINER.start();
    }

    System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
    System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
  }
}
