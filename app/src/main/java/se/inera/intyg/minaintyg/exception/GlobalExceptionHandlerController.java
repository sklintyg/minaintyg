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
package se.inera.intyg.minaintyg.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.inera.intyg.minaintyg.integration.common.IllegalCertificateAccessException;
import se.inera.intyg.minaintyg.integration.common.IntegrationServiceException;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerController {

  private final MonitoringLogService monitoringLogService;

  @ExceptionHandler(IntegrationServiceException.class)
  public ResponseEntity<String> handleCommunicationError(IntegrationServiceException exception) {
    log.error(
        "Unable to establish integration with '{}' stacktrace {}",
        exception.getApplicationName(),
        exception.getStackTrace());

    return ResponseEntity.status(503).build();
  }

  @ExceptionHandler(IllegalCertificateAccessException.class)
  public ResponseEntity<String> handleAccessException(IllegalCertificateAccessException exception) {
    monitoringLogService.logIllegalCertificateAccess(exception.getMessage());
    return ResponseEntity.status(403).build();
  }
}
