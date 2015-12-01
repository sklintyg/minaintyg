package se.inera.intyg.minaintyg.web.logging;

import ch.qos.logback.classic.PatternLayout;

/**
 * Logback {@link PatternLayout} PatternLayout implementation that exposes
 * session information.
 *
 * @author nikpet
 */
public class PatternLayoutWithSessionContext extends PatternLayout {
    static {
        PatternLayout.defaultConverterMap.put(
                "session", SessionConverter.class.getName());
    }
}
