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
package se.inera.intyg.minaintyg.integration.common;

import static se.inera.intyg.minaintyg.logging.MdcHelper.LOG_SESSION_ID_HEADER;
import static se.inera.intyg.minaintyg.logging.MdcHelper.LOG_TRACE_ID_HEADER;
import static se.inera.intyg.minaintyg.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.minaintyg.logging.MdcLogConstants.TRACE_ID_KEY;

import org.slf4j.MDC;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class ExchangeFilterFunctionProvider {

  private ExchangeFilterFunctionProvider() {
    throw new IllegalStateException("Utility class");
  }

  public static ExchangeFilterFunction addHeadersFromMDCToRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(
        request ->
            Mono.just(
                ClientRequest.from(request)
                    .headers(
                        httpHeaders -> {
                          httpHeaders.add(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY));
                          httpHeaders.add(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY));
                        })
                    .build()));
  }
}
