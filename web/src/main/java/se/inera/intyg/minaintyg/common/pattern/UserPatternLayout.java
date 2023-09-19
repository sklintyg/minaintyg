package se.inera.intyg.minaintyg.common.pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

public class UserPatternLayout extends PatternLayoutEncoder {

  static {
    PatternLayout.DEFAULT_CONVERTER_MAP.put("user", UserConverter.class.getName());
  }
}
