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
package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@ExtendWith(MockitoExtension.class)
class CertificateEventServiceTest {

  private static final CertificateRecipientDTO SENT_RECIPIENT =
      CertificateRecipientDTO.builder().id("id").name("Namn").sent(LocalDateTime.now()).build();
  private static final CertificateRecipientDTO NOT_SENT_RECIPIENT =
      CertificateRecipientDTO.builder().id("id").build();
  private static final List<CertificateRelationDTO> REPLACED_RELATIONS =
      List.of(
          CertificateRelationDTO.builder()
              .type(CertificateRelationType.REPLACED)
              .certificateId("certificateId1")
              .timestamp(LocalDateTime.now())
              .build());
  private static final List<CertificateRelationDTO> REPLACES_RELATIONS =
      List.of(
          CertificateRelationDTO.builder()
              .type(CertificateRelationType.REPLACES)
              .certificateId("certificateId2")
              .timestamp(LocalDateTime.now())
              .build());

  @InjectMocks CertificateEventService certificateEventService;

  @Test
  void shouldNotIncludeSeveralStatuses() {
    final var response = certificateEventService.get(REPLACED_RELATIONS, SENT_RECIPIENT);

    assertEquals(2, response.size());
  }

  @Nested
  class SentEvent {

    @Test
    void shouldNotIncludeEventIfRecipientIsNull() {
      final var response = certificateEventService.get(Collections.emptyList(), null);

      assertEquals(0, response.size());
    }

    @Test
    void shouldIncludeEventIfNotSent() {
      final var response = certificateEventService.get(Collections.emptyList(), NOT_SENT_RECIPIENT);

      assertEquals(0, response.size());
    }

    @Test
    void shouldIncludeEventIfSent() {
      final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

      assertEquals(1, response.size());
    }

    @Test
    void shouldIncludeDescription() {
      final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

      assertEquals("Skickat till Namn", response.get(0).getDescription());
    }

    @Test
    void shouldIncludeTimestamp() {
      final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

      assertEquals(SENT_RECIPIENT.getSent(), response.get(0).getTimestamp());
    }

    @Test
    void shouldNotIncludeCertificateId() {
      final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

      assertNull(response.get(0).getCertificateId());
    }
  }

  @Nested
  class ReplacedEvent {

    @Test
    void shouldIncludeEventIfRelationExists() {
      final var response = certificateEventService.get(REPLACED_RELATIONS, null);

      assertEquals(1, response.size());
    }

    @Test
    void shouldIncludeDescription() {
      final var response = certificateEventService.get(REPLACED_RELATIONS, null);

      assertEquals("Ersattes av vården med ett nytt intyg", response.get(0).getDescription());
    }

    @Test
    void shouldIncludeCertificateId() {
      final var response = certificateEventService.get(REPLACED_RELATIONS, null);

      assertEquals(
          REPLACED_RELATIONS.get(0).getCertificateId(), response.get(0).getCertificateId());
    }

    @Test
    void shouldIncludeTimestamp() {
      final var response = certificateEventService.get(REPLACED_RELATIONS, null);

      assertEquals(REPLACED_RELATIONS.get(0).getTimestamp(), response.get(0).getTimestamp());
    }
  }

  @Nested
  class ReplacesEvent {

    @Test
    void shouldIncludeEventIfRelationExists() {
      final var response = certificateEventService.get(REPLACES_RELATIONS, null);

      assertEquals(1, response.size());
    }

    @Test
    void shouldIncludeDescription() {
      final var response = certificateEventService.get(REPLACES_RELATIONS, null);

      assertEquals(
          "Ersätter ett intyg som inte längre är aktuellt", response.get(0).getDescription());
    }

    @Test
    void shouldIncludeCertificateId() {
      final var response = certificateEventService.get(REPLACES_RELATIONS, null);

      assertEquals(
          REPLACES_RELATIONS.get(0).getCertificateId(), response.get(0).getCertificateId());
    }

    @Test
    void shouldIncludeTimestamp() {
      final var response = certificateEventService.get(REPLACES_RELATIONS, null);

      assertEquals(REPLACES_RELATIONS.get(0).getTimestamp(), response.get(0).getTimestamp());
    }
  }
}
