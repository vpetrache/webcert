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

package se.inera.intyg.webcert.web.service.utkast.dto;

import java.util.ArrayList;
import java.util.List;

public class DraftValidation {

    private DraftValidationStatus status = DraftValidationStatus.VALID;

    private List<DraftValidationMessage> messages = new ArrayList<>();

    public DraftValidation() {
    }

    public DraftValidationStatus getStatus() {
        return status;
    }

    public void setStatus(DraftValidationStatus status) {
        this.status = status;
    }

    public void addMessage(DraftValidationMessage message) {
        this.messages.add(message);
    }

    public List<DraftValidationMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<DraftValidationMessage> messages) {
        this.messages = messages;
    }

    public boolean isDraftValid() {
        return (DraftValidationStatus.VALID.equals(this.status));
    }
}