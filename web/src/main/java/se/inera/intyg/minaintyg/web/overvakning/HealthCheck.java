/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.overvakning;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheck.class);

    public Status getPing() {
        boolean ok;
        Stopwatch sp = Stopwatch.createStarted();
        try {
            ok = true;
        } catch (Exception e) {
            ok = false;
        }
        sp.stop();
        long elapsedMillis = sp.elapsed(TimeUnit.MILLISECONDS);
        LOGGER.info("Ping in Mina Intyg got:{} response time {}", ok, elapsedMillis);
        return createStatus(ok, elapsedMillis);
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
