/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import se.inera.clinicalprocess.healthcond.certificate.v1.ResultType;


public class ResultTypeErrorException extends RuntimeException {

    private static final long serialVersionUID = -412823459027541760L;

    private final ResultType resultType;

    public ResultTypeErrorException(ResultType resultType) {
        this.resultType = resultType;
    }

    @Override
    public String getMessage() {
        return "Failed to invoke internal web service. Result type is " + resultType;
    }

    public ResultType getResultType() {
        return resultType;
    }
}
