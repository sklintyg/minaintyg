/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;

/**
 * Exception throws if a call to another RIV-TA web service does return with a result code
 * INFO or ERROR.
 * The returned {@link ResultOfCall} is available within the exception to allow proper exception handling
 * for the executing client.
 *
 * @author andreaskaltenbach
 */
public class ExternalWebServiceCallFailedException extends RuntimeException {

    private final ResultOfCall resultOfCall;

    public ExternalWebServiceCallFailedException(ResultOfCall resultOfCall) {
        this.resultOfCall = resultOfCall;
    }

    @Override
    public String getMessage() {
        return "Failed to invoke internal web service. Result of call is " + resultOfCall;
    }

    public ResultOfCall getResultOfCall() {
        return resultOfCall;
    }
}
