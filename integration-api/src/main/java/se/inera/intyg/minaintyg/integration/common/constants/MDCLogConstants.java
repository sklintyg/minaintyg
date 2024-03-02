package se.inera.intyg.minaintyg.integration.common.constants;

public class MDCLogConstants {

  private MDCLogConstants() {

  }

  public static final String LOG_TRACE_ID_HEADER = "x-trace-id";
  public static final String LOG_SESSION_ID_HEADER = "x-session-id";
  public static final String MDC_TRACE_ID_KEY = "req.traceId";
  public static final String MDC_SESSION_ID_KEY = "req.sessionInfo";
}
