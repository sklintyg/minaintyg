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
package se.inera.intyg.minaintyg.monitoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.logging.LogController;
import se.inera.intyg.minaintyg.logging.dto.LogErrorRequestDTO;
import se.inera.intyg.minaintyg.logging.service.LogErrorService;
import se.inera.intyg.minaintyg.logging.service.dto.LogErrorRequest;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

  private static final LogErrorRequestDTO REQUEST =
      LogErrorRequestDTO.builder()
          .id("id")
          .message("message")
          .code("code")
          .stackTrace("stackTrace")
          .build();

  @Mock private LogErrorService logErrorService;

  @InjectMocks private LogController logController;

  @Test
  void shouldCallServiceWithErrorId() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getId(), captor.getValue().getId());
  }

  @Test
  void shouldCallServiceWithErrorCode() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getCode(), captor.getValue().getCode());
  }

  @Test
  void shouldCallServiceWithErrorMessage() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getMessage(), captor.getValue().getMessage());
  }

  @Test
  void shouldCallServiceWithErrorStackTrace() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getStackTrace(), captor.getValue().getStackTrace());
  }
}
