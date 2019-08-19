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
package se.inera.intyg.minaintyg.web.service;

import io.prometheus.client.Collector;
import io.prometheus.client.Gauge;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Exposes health metrics as Prometheus values. To simplify any 3rd party scraping applications, all metrics produced
 * by this component uses the following conventions:
 *
 * All metrics are prefixed with "health_"
 * All metrics are suffixed with their type, either "_normal" that indicates a boolean value 0 or 1 OR
 * "_value" that indiciates a numeric metric of some kind.
 *
 * Note that NORMAL values uses 0 to indicate OK state and 1 to indicate a problem.
 *
 * @author eriklupander
 */
@Component
public class HealthMonitor extends Collector {

    private static final String PREFIX = "health_";
    private static final String NORMAL = "_normal";
    private static final String VALUE = "_value";

    private static final long START_TIME = System.currentTimeMillis();

    private static final Gauge UPTIME = Gauge.build()
        .name(PREFIX + "uptime" + VALUE)
        .help("Current uptime in seconds")
        .register();

    private static final Gauge IT_ACCESSIBLE = Gauge.build()
        .name(PREFIX + "intygstjanst_accessible" + NORMAL)
        .help("0 == OK 1 == NOT OK")
        .register();

    private static final long MILLIS_PER_SECOND = 1000L;

    @Value("${certificates.metrics.url}")
    private String itMetricsUrl;

    @FunctionalInterface
    interface Tester {

        void run() throws Exception;
    }

    /**
     * Registers this class as a prometheus collector.
     */
    @PostConstruct
    public void init() {
        this.register();
    }

    /**
     * Somewhat hacky way of updating our gauges "on-demand" (each being registered itself as a collector),
     * with this method always returning an empty list of MetricFamilySamples.
     *
     * @return Always returns an empty list.
     */
    @Override
    public List<MetricFamilySamples> collect() {
        long secondsSinceStart = (System.currentTimeMillis() - START_TIME) / MILLIS_PER_SECOND;

        // Update the gauges.
        UPTIME.set(secondsSinceStart);
        IT_ACCESSIBLE.set(pingIntygstjanst() ? 0 : 1);

        return Collections.emptyList();
    }

    private boolean invoke(Tester tester) {
        try {
            tester.run();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean pingIntygstjanst() {
        return invoke(() -> {
            HttpURLConnection httpConnection = (HttpURLConnection) new URL(itMetricsUrl).openConnection();
            int respCode = httpConnection.getResponseCode();
            httpConnection.disconnect();
            if (respCode != HttpServletResponse.SC_OK) {
                throw new RuntimeException();
            }
        });
    }
}
