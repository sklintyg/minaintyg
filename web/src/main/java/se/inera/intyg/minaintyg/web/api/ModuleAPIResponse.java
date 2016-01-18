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

package se.inera.intyg.minaintyg.web.api;

import java.io.Serializable;

/**
 * Generic Response Result holder for module API requests.
 * Subclasses can extend with custom properties as needed.
 * @author marced
 *
 */
@SuppressWarnings("serial")
public class ModuleAPIResponse implements Serializable {
    private String resultCode;
    private String errorCode;

    public ModuleAPIResponse(String result, String errorCode) {
        super();
        this.resultCode = result;
        this.errorCode = errorCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String result) {
        this.resultCode = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
