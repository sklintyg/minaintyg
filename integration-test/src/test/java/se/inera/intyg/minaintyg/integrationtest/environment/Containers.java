/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.integrationtest.environment;

import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class Containers {

  public static MockServerContainer mockServerContainer;
  public static GenericContainer<?> redisContainer;
  public static ActiveMQContainer amqContainer;

  public static void ensureRunning() {
    mockServerContainer();
    redisContainer();
    amqContainer();
  }

  private static void mockServerContainer() {
    if (mockServerContainer == null) {
      mockServerContainer =
          new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));
    }

    if (!mockServerContainer.isRunning()) {
      mockServerContainer.start();
    }

    final var mockServerContainerHost = mockServerContainer.getHost();
    final var mockServerContainerPort = String.valueOf(mockServerContainer.getServerPort());

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
    if (redisContainer == null) {
      redisContainer =
          new GenericContainer<>(DockerImageName.parse("redis:6.0.9-alpine"))
              .withExposedPorts(6379);
    }

    if (!redisContainer.isRunning()) {
      redisContainer.start();
    }

    System.setProperty("spring.data.redis.host", redisContainer.getHost());
    System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString());
  }

  private static void amqContainer() {
    if (amqContainer == null) {
      amqContainer =
          new ActiveMQContainer("apache/activemq-classic:5.18.3")
              .withUser("activemqUser")
              .withPassword("activemqPassword");
    }

    if (!amqContainer.isRunning()) {
      amqContainer.start();
    }

    System.setProperty("spring.activemq.user", amqContainer.getUser());
    System.setProperty("spring.activemq.password", amqContainer.getPassword());
    System.setProperty("spring.activemq.broker-url", amqContainer.getBrokerUrl());
  }
}
