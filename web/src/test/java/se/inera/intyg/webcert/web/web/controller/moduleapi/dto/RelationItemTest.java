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
package se.inera.intyg.webcert.web.web.controller.moduleapi.dto;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.RelationKod;
import se.inera.intyg.webcert.persistence.utkast.model.*;

public class RelationItemTest {

    @Test
    public void testConstructor() {
        final String intygsId = "intygsId";

        Signatur signatur = new Signatur(LocalDateTime.now(), "", intygsId, "", "", "");

        Utkast utkast1 = new Utkast();
        utkast1.setIntygsId(intygsId);
        utkast1.setRelationKod(RelationKod.KOMPLT);
        utkast1.setStatus(UtkastStatus.DRAFT_COMPLETE);
        RelationItem ri1 = new RelationItem(utkast1);

        Utkast utkast2 = new Utkast();
        utkast2.setStatus(UtkastStatus.DRAFT_INCOMPLETE);
        RelationItem ri2 = new RelationItem(utkast2);

        Utkast utkast3 = new Utkast();
        utkast3.setStatus(UtkastStatus.SIGNED);
        utkast3.setSignatur(signatur);
        RelationItem ri3 = new RelationItem(utkast3);

        Utkast utkast4 = new Utkast();
        utkast4.setStatus(UtkastStatus.SIGNED);
        utkast4.setSignatur(signatur);
        utkast4.setAterkalladDatum(LocalDateTime.now());
        RelationItem ri4 = new RelationItem(utkast4);

        Utkast utkast5 = new Utkast();
        utkast5.setStatus(UtkastStatus.SIGNED);
        utkast5.setSignatur(signatur);
        utkast5.setSkickadTillMottagareDatum(LocalDateTime.now());
        RelationItem ri5 = new RelationItem(utkast5);

        assertEquals(intygsId, ri1.getIntygsId());
        assertEquals(RelationKod.KOMPLT.value(), ri1.getKod());
        assertEquals("DRAFT_COMPLETE", ri1.getStatus());
        assertEquals("DRAFT_INCOMPLETE", ri2.getStatus());
        assertEquals("RECEIVED", ri3.getStatus());
        assertEquals("CANCELLED", ri4.getStatus());
        assertEquals("SENT", ri5.getStatus());
    }
}
