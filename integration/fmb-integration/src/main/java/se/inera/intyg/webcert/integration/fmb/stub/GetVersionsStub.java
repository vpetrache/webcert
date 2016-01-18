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

package se.inera.intyg.webcert.integration.fmb.stub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.riv.processmanagement.decisionsupport.insurancemedicinedecisionsupport.getversionsresponder.v1.GetVersionsResponderInterface;
import se.riv.processmanagement.decisionsupport.insurancemedicinedecisionsupport.getversionsresponder.v1.GetVersionsResponseType;
import se.riv.processmanagement.decisionsupport.insurancemedicinedecisionsupport.getversionsresponder.v1.GetVersionsType;
import se.riv.processmanagement.decisionsupport.insurancemedicinedecisionsupport.v1.VersionerType;

public class GetVersionsStub implements GetVersionsResponderInterface {

    private static final Logger LOG = LoggerFactory.getLogger(GetVersionsStub.class);

    public GetVersionsStub() {
        LOG.info("Starting stub: FMB GetVersionsStub");
    }

    @Override
    public GetVersionsResponseType getVersions(String s, GetVersionsType getVersionsType) {
        final GetVersionsResponseType versionsResponse = new GetVersionsResponseType();
        final VersionerType versioner = new VersionerType();
        versioner.setFmbSenateAndring(String.valueOf(System.currentTimeMillis()));
        versioner.setDiagnosInformationSenateAndring(String.valueOf(System.currentTimeMillis()));
        versionsResponse.setVersioner(versioner);
        return versionsResponse;
    }

}
