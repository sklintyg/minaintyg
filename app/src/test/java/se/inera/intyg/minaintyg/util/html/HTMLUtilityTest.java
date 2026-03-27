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
package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class HTMLUtilityTest {

  private static final String S1 = "S_1";
  private static final String S2 = "S_2";
  private static final String S3 = "S_3";

  @Test
  void shouldJoinTwoTexts() {
    final var response = HTMLUtility.join(S1, S2);

    assertEquals(response, S1 + S2);
  }

  @Test
  void shouldJoinThreeTexts() {
    final var response = HTMLUtility.join(S1, S2, S3);

    assertEquals(response, S1 + S2 + S3);
  }

  @Test
  void shouldNotJoinNull() {
    final var response = HTMLUtility.join(S1, S2, S3, null);

    assertEquals(response, S1 + S2 + S3);
  }

  @Test
  void shouldFormatTextsInList() {
    final var response = HTMLUtility.fromList(List.of(S1, S2), HTMLTextFactory::p);

    assertEquals(response, "<p>" + S1 + "</p><p>" + S2 + "</p>");
  }

  @Test
  void shouldReturEmptyStringIfListIsEmpty() {
    final var response = HTMLUtility.fromList(Collections.emptyList(), HTMLTextFactory::p);

    assertEquals("", response);
  }
}
