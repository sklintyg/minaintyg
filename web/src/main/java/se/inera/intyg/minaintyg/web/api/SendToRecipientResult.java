/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
import java.time.LocalDateTime;

/**
 * Represents the result of sending a certificate to a specific recipient.
 *
 * Created by marced on 2017-03-15.
 */
public class SendToRecipientResult implements Serializable {

    private final String recipientId;
    private final boolean sent;
    private final LocalDateTime timestamp;

    public SendToRecipientResult(String recipientId, boolean sent, LocalDateTime timestamp) {
        this.recipientId = recipientId;
        this.sent = sent;
        this.timestamp = timestamp;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public boolean isSent() {
        return sent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
