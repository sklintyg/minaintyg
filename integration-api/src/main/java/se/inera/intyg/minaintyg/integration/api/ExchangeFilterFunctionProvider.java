package se.inera.intyg.minaintyg.integration.api;

import static se.inera.intyg.minaintyg.integration.api.constants.MDCLogConstants.LOG_SESSION_ID_HEADER;
import static se.inera.intyg.minaintyg.integration.api.constants.MDCLogConstants.LOG_TRACE_ID_HEADER;
import static se.inera.intyg.minaintyg.integration.api.constants.MDCLogConstants.MDC_SESSION_ID_KEY;
import static se.inera.intyg.minaintyg.integration.api.constants.MDCLogConstants.MDC_TRACE_ID_KEY;

import org.slf4j.MDC;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class ExchangeFilterFunctionProvider {

  public static ExchangeFilterFunction getExchangeFilterFunction() {
    return ExchangeFilterFunction.ofRequestProcessor(
        request -> Mono.just(ClientRequest.from(request)
            .headers(httpHeaders -> {
              httpHeaders.add(LOG_TRACE_ID_HEADER, MDC.get(MDC_TRACE_ID_KEY));
              httpHeaders.add(LOG_SESSION_ID_HEADER, MDC.get(MDC_SESSION_ID_KEY));
            })
            .build())
    );
  }
}
