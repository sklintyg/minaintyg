/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.error.dto.ErrorDataDTO;
import se.inera.intyg.minaintyg.error.dto.LogErrorRequestDTO;
import se.inera.intyg.minaintyg.error.service.LogErrorService;
import se.inera.intyg.minaintyg.error.service.dto.LogErrorRequest;

@ExtendWith(MockitoExtension.class)
class ErrorControllerTest {

  private static final LogErrorRequestDTO REQUEST = LogErrorRequestDTO.builder()
      .error(ErrorDataDTO.builder()
          .id("id")
          .message("message")
          .code("code")
          .stackTrace("stackTrace")
          .build())
      .build();

  @Mock
  private LogErrorService logErrorService;

  @InjectMocks
  private ErrorController errorController;

  @Test
  void shouldCallServiceWithErrorId() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getId(), captor.getValue().getError().getId());
  }

  @Test
  void shouldCallServiceWithErrorCode() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getCode(), captor.getValue().getError().getCode());
  }

  @Test
  void shouldCallServiceWithErrorMessage() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getMessage(), captor.getValue().getError().getMessage());
  }

  @Test
  void shouldCallServiceWithErrorStackTrace() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getStackTrace(), captor.getValue().getError().getStackTrace());
  }
}