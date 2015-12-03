package se.inera.intyg.minaintyg.web.web.service.dto;

public class HealthStatus {

    private final long measurement;

    private final boolean ok;

    public HealthStatus(long measurement, boolean ok) {
        this.measurement = measurement;
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public long getMeasurement() {
        return measurement;
    }
}
