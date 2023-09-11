package se.inera.intyg.minaintyg.monitoring;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class LogMarkers {

  public static final Marker VALIDATION = MarkerFactory.getMarker("Validation");
  public static final Marker MONITORING = MarkerFactory.getMarker("Monitoring");

  private LogMarkers() {
  }
}
