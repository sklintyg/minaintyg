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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

class CertificateStatusFactoryTest {

  private static final CertificateRelationDTO replaced =
      CertificateRelationDTO.builder()
          .certificateId("CERTIFICATE_ID")
          .timestamp(LocalDateTime.now())
          .type(CertificateRelationType.REPLACED)
          .build();

  private static final CertificateRelationDTO replaces =
      CertificateRelationDTO.builder()
          .certificateId("CERTIFICATE_ID")
          .timestamp(LocalDateTime.now())
          .type(CertificateRelationType.REPLACES)
          .build();

  private static final CertificateRecipientDTO recipient =
      CertificateRecipientDTO.builder().name("Name").id("id").sent(LocalDateTime.now()).build();

  @Nested
  class ReplacedEvent {

    @Test
    void shouldReturnReplacedIfCorrectRelationType() {
      final var result = CertificateStatusFactory.replaced(replaced);

      assertEquals(CertificateStatusType.REPLACED, result.get());
    }

    @Test
    void shouldReturnEmptyIfRelationIsNotReplaced() {
      final var result = CertificateStatusFactory.replaced(replaces);

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfRelationIsNull() {
      final var result = CertificateStatusFactory.replaced(null);

      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class SentEvent {

    @Test
    void shouldReturnSentIfRecipientHasSentTimestamp() {
      final var result = CertificateStatusFactory.sent(recipient);

      assertEquals(CertificateStatusType.SENT, result.get());
    }

    @Test
    void shouldReturnEmptyIfRecipientIsNull() {
      final var result = CertificateStatusFactory.sent(null);

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnNotSentIfNoSentValue() {
      final var result = CertificateStatusFactory.sent(CertificateRecipientDTO.builder().build());

      assertEquals(CertificateStatusType.NOT_SENT, result.get());
    }
  }

  @Nested
  class NewEvent {

    @Test
    void shouldReturnNewIfIssuedIsLessThanFourteenDaysAgo() {
      final var result = CertificateStatusFactory.newStatus(LocalDateTime.now());

      assertEquals(CertificateStatusType.NEW, result.get());
    }

    @Test
    void shouldReturnEmptyIfIssuedIsMoreThanFourteenDaysAgo() {
      final var result = CertificateStatusFactory.newStatus(LocalDateTime.now().minusDays(15));

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnNewIfIssuedIsFourteenDaysAgo() {
      final var result = CertificateStatusFactory.newStatus(LocalDateTime.now().minusDays(14));

      assertEquals(CertificateStatusType.NEW, result.get());
    }
  }
}
