package se.inera.intyg.minaintyg.web.web.overvakning;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheck.class);

    public Status getPing() {
        boolean ok;
        StopWatch sp = new StopWatch();
        sp.start();
        try {
            ok = true;
        } catch (Exception e) {
            ok = false;
        }
        sp.stop();
        LOGGER.info("Ping in Mina Intyg got:{} response time {}", ok, sp.getTime());
        return createStatus(ok, sp.getTime());
    }

    private Status createStatus(boolean ok, long time) {
        return new Status(time, ok);
    }

    public static final class Status {
        private final long measurement;
        private final boolean ok;

        private Status(long measurement, boolean ok) {
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
}
