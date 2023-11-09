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

package se.inera.intyg.minaintyg.error.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.error.service.dto.ErrorData;
import se.inera.intyg.minaintyg.error.service.dto.LogErrorRequest;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@ExtendWith(MockitoExtension.class)
class LogErrorServiceTest {

  private static final ErrorData ERROR = ErrorData.builder()
      .id("ID")
      .code("CODE")
      .message("MESSAGE")
      .stackTrace("STACK TRACE")
      .build();

  private static final LogErrorRequest REQUEST = LogErrorRequest.builder()
      .error(ERROR)
      .build();

  @Mock
  MonitoringLogService monitoringLogService;

  @Mock
  UserService userService;

  @InjectMocks
  private LogErrorService logErrorService;

  @Test
  void shouldThrowErrorIfUserIsNotDefined() {
    assertThrows(IllegalStateException.class, () -> logErrorService.log(REQUEST));
  }

  @Nested
  class HasUser {

    @BeforeEach
    void setup() {
      when(userService.getLoggedInUser())
          .thenReturn(Optional.of(MinaIntygUser.builder().build()));
    }

    @Test
    void shouldLogUsingId() {
      final var captor = ArgumentCaptor.forClass(String.class);

      logErrorService.log(REQUEST);

      verify(monitoringLogService).logClientError(
          captor.capture(),
          anyString(),
          anyString(),
          anyString()
      );
      assertEquals(ERROR.getId(), captor.getValue());
    }

    @Test
    void shouldLogUsingCode() {
      final var captor = ArgumentCaptor.forClass(String.class);

      logErrorService.log(REQUEST);

      verify(monitoringLogService).logClientError(
          anyString(),
          captor.capture(),
          anyString(),
          anyString()
      );
      assertEquals(ERROR.getCode(), captor.getValue());
    }

    @Test
    void shouldLogUsingMessage() {
      final var captor = ArgumentCaptor.forClass(String.class);

      logErrorService.log(REQUEST);

      verify(monitoringLogService).logClientError(
          anyString(),
          anyString(),
          captor.capture(),
          anyString()
      );
      assertEquals(ERROR.getMessage(), captor.getValue());
    }

    @Test
    void shouldLogUsingStackTrace() {
      final var captor = ArgumentCaptor.forClass(String.class);

      logErrorService.log(REQUEST);

      verify(monitoringLogService).logClientError(
          anyString(),
          anyString(),
          anyString(),
          captor.capture()
      );
      assertEquals(ERROR.getStackTrace(), captor.getValue());
    }
  }
}