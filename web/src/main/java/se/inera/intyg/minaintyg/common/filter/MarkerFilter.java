package se.inera.intyg.minaintyg.common.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import se.inera.intyg.minaintyg.common.logging.LogMarkers;

public class MarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

  public static final Marker MONITORING = MarkerFactory.getMarker(LogMarkers.MONITORING.getName());

  private final List<Marker> markersToMatch = new ArrayList<>();

  @Override
  public void start() {
    if (!this.markersToMatch.isEmpty()) {
      super.start();
    } else {
      addError("!!! no marker yet !!!");
    }
  }

  @Override
  public FilterReply decide(final ILoggingEvent event) {
    if (!isStarted()) {
      return FilterReply.NEUTRAL;
    }

    final var marker = event.getMarkerList();
    return Objects.nonNull(marker)
        && this.markersToMatch.stream()
        .anyMatch(m -> marker.stream().anyMatch(mark -> mark == m)) ? getOnMatch()
        : getOnMismatch();
  }

  public void setMarker(final String name) {
    if (!Strings.isNullOrEmpty(name)) {
      this.markersToMatch.add(MarkerFactory.getMarker(name));
    }
  }

  public void setMarkers(final String names) {
    if (!Strings.isNullOrEmpty(names)) {
      Splitter.on(",").split(names)
          .forEach(n -> this.markersToMatch.add(MarkerFactory.getMarker(n.trim())));
    }
  }
}
