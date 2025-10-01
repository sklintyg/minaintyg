package se.inera.intyg.minaintyg.logging;

public class MdcLogConstants {

  private MdcLogConstants() {

  }
  
  public static final String ERROR_ID = "error.id";
  public static final String ERROR_CODE = "error.code";
  public static final String ERROR_MESSAGE = "error.message";
  public static final String ERROR_STACKTRACE = "error.stack_trace";
  public static final String EVENT_RECIPIENT = "event.recipient";
  public static final String EVENT_ACTION = "event.action";
  public static final String EVENT_CATEGORY = "event.category";
  public static final String EVENT_CATEGORY_API = "[api]";
  public static final String EVENT_CATEGORY_DATABASE = "[database]";
  public static final String EVENT_CATEGORY_PROCESS = "[process]";
  public static final String EVENT_CATEGORY_INTRUSION_DETECTION = "[intrusion_detection]";
  public static final String EVENT_TYPE = "event.type";
  public static final String EVENT_START = "event.start";
  public static final String EVENT_END = "event.end";
  public static final String EVENT_DURATION = "event.duration";
  public static final String EVENT_CLASS = "event.class";
  public static final String EVENT_METHOD = "event.method";
  public static final String EVENT_OUTCOME = "event.outcome";
  public static final String EVENT_OUTCOME_DENIED = "denied";
  public static final String EVENT_CERTIFICATE_ID = "event.certificate.id";
  public static final String EVENT_CERTIFICATE_TYPE = "event.certificate.type";
  public static final String EVENT_CERTIFICATE_CARE_UNIT_ID = "event.certificate.care_unit.id";
  public static final String EVENT_LOGIN_METHOD = "event.login.method";
  public static final String EVENT_MESSAGE_ID = "event.message.id";
  public static final String EVENT_MESSAGE_TOPIC = "event.message.topic";
  public static final String EVENT_PART_ID = "event.part.id";
  public static final String SESSION_ID_KEY = "session.id";
  public static final String SPAN_ID_KEY = "span.id";
  public static final String TRACE_ID_KEY = "trace.id";
  public static final String USER_ID = "user.id";

  public static final String EVENT_TYPE_CHANGE = "change";
  public static final String EVENT_TYPE_DELETION = "deletion";
  public static final String EVENT_TYPE_CREATION = "creation";
  public static final String EVENT_TYPE_ACCESSED = "accessed";
  public static final String EVENT_TYPE_INFO = "info";
}
