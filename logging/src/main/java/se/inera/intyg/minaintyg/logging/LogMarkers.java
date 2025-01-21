package se.inera.intyg.minaintyg.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class LogMarkers {

  public static final Marker MONITORING = MarkerFactory.getMarker("Monitoring");
  public static final Marker PERFORMANCE = MarkerFactory.getMarker("Performance");

  private LogMarkers() {
  }

}
