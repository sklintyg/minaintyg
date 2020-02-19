/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.monitoring;

import java.util.Map;

public class MonitoringRequest {


    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String QUESTION_ID = "id";
    public static final String QUESTION_TITLE = "title";
    public static final String USER = "user";

    private MonitoringRequestEvent event;
    private Map<String, String> info;

    public MonitoringRequestEvent getEvent() {
        return event;
    }

    public void setEvent(MonitoringRequestEvent event) {
        this.event = event;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    public boolean isValid() {
        if (event == null) {
            return false;
        }
        switch (event) {
            case SCREEN_RESOLUTION:
                return info != null && info.get(WIDTH) != null && info.get(HEIGHT) != null;
            case OPENED_ABOUT:
            case OPENED_FAQ:
                return info != null && info.get(USER) != null;
            case OPENED_QUESTION:
                return info != null && info.get(QUESTION_ID) != null && info.get(QUESTION_TITLE) != null && info.get(USER) != null;
            default:
                return false;
        }
    }

    public enum MonitoringRequestEvent {
        SCREEN_RESOLUTION,
        OPENED_ABOUT,
        OPENED_FAQ,
        OPENED_QUESTION
    }
}
