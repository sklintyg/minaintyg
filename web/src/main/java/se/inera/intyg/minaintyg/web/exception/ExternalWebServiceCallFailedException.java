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
package se.inera.intyg.minaintyg.web.exception;

/**
 * Exception throws if a call to another RIV-TA web service does return with a result code
 * INFO or ERROR.
 *
 * @author andreaskaltenbach
 */
public class ExternalWebServiceCallFailedException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 6518728574652543260L;
    private final String resultText;
    private final String errorId;

    public ExternalWebServiceCallFailedException(String resultText, String errorId) {
        this.resultText = resultText;
        this.errorId = errorId;
    }

    @Override
    public String getMessage() {
        return String.format("Failed to invoke internal web service. Result of call is %s, errorId: %s.", resultText, errorId);
    }

    public String getInfo() {
        return resultText;
    }

    public String getErrorId() {
        return errorId;
    }
}
