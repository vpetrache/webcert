/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.webcert.web.service.intyg.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.webcert.web.web.controller.moduleapi.dto.RelationItem;

public class IntygContentHolder {

    @JsonRawValue
    private final String contents;

    @JsonIgnore
    private final Utlatande utlatande;
    private final List<Status> statuses;
    private final boolean revoked;
    private final List<RelationItem> relations;
    private final RelationItem replacedByRelation;
    private final boolean deceased;

    public IntygContentHolder(String contents, Utlatande utlatande, List<Status> statuses, boolean revoked, List<RelationItem> relations,
            RelationItem replacedByRelation,
            boolean deceased) {
        this.contents = contents;
        this.utlatande = utlatande;
        this.statuses = statuses;
        this.revoked = revoked;
        if (relations == null) {
            this.relations = new ArrayList<>();
        } else {
            this.relations = relations;
        }
        this.replacedByRelation = replacedByRelation;
        this.deceased = deceased;
    }

    public String getContents() {
        return contents;
    }

    public Utlatande getUtlatande() {
        return utlatande;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public List<RelationItem> getRelations() {
        return relations;
    }

    public RelationItem getReplacedByRelation() {
        return replacedByRelation;
    }
    public boolean isDeceased() {
        return deceased;
    }


}
