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
package se.inera.intyg.webcert.web.service.intyg;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intyg.webcert.persistence.utkast.model.Utkast;
import se.inera.intyg.webcert.web.service.intyg.dto.IntygContentHolder;
import se.inera.intyg.webcert.web.service.intyg.dto.IntygPdf;
import se.inera.intyg.webcert.web.service.intyg.dto.IntygServiceResult;
import se.inera.intyg.webcert.web.service.intyg.dto.IntygWithNotificationsRequest;
import se.inera.intyg.webcert.web.service.intyg.dto.IntygWithNotificationsResponse;
import se.inera.intyg.webcert.web.web.controller.api.dto.ListIntygEntry;

/**
 * @author andreaskaltenbach
 */
public interface IntygService {

    /**
     * Fetches the intyg data from the Intygstjanst and returns the intyg content in internal model representation.
     *
     * If the Intygstjanst couldn't find the intyg or the Intygstjanst was not available,
     * an attempt to find an utkast stored in Webcert will be performed.
     */
    IntygContentHolder fetchIntygData(String intygId, String typ, boolean coherentJournaling);

    /**
     * Fetches the intyg data from the Intygstjanst and returns the intyg content in internal model representation.
     *
     * If the Intygstjanst couldn't find the intyg or the Intygstjanst was not available,
     * an attempt to find an utkast stored in Webcert will be performed.
     *
     * Also includes a list of the relations the intyg has to other intyg.
     */
    IntygContentHolder fetchIntygDataWithRelations(String intygId, String typ, boolean coherentJournaling);

    /**
     * Returns all certificates for the given patient within all the given units.
     *
     * @param enhetId
     *            list of HSA IDs for the units
     * @param personnummer
     *            the person number
     * @return list of certificates matching the search criteria wrapped in a response container also indicating whether
     *         the data was fetched from intygstjansten ("online") or from webcert ("offline").
     */
    Pair<List<ListIntygEntry>, Boolean> listIntyg(List<String> enhetId, Personnummer personnummer);

    /**
     * Returns a given certificate as PDF.
     *
     * @param isEmployer
     *            Indicates if the certificate should be for the employer.
     */
    IntygPdf fetchIntygAsPdf(String intygId, String typ, boolean isEmployer);

    /**
     * Registers a given certificate in the Intygstjanst.
     */
    IntygServiceResult storeIntyg(Utkast utkast);

    /**
     * Instructs Intygstjanst to deliver the given certifiate to an external recipient.
     */
    IntygServiceResult sendIntyg(String intygId, String typ, String mottagare);

    /**
     * Instructs Intygstjanst to revoke the given certificate.
     */
    IntygServiceResult revokeIntyg(String intygId, String intygTyp, String revokeMessage, String reason);

    /**
     * Handle a signed completion, i.e., send the certificate to its recipient and close all pending completion QA /
     * Arende as handled.
     */
    void handleSignedCompletion(Utkast utkast, String recipient);

    /**
     * Retrieves the hsaId of the vardenhet this intyg is issued on.
     *
     * @param intygId
     * @param intygsTyp
     * @return
     *         The HSA Id of the vardenhet where the intyg was created/issued.
     */
    String getIssuingVardenhetHsaId(String intygId, String intygsTyp);

    /**
     * Checks if the specified Intyg is revoked or not. If the Intygstjanst couldn't find the intyg or the Intygstjanst
     * is not available, an attempt to find the revoke status on an utkast stored in Webcert will be performed.
     *
     * This method will not yield a PDL statement as it doesn't actually show anything to an end-user. It will however
     * log using {@link se.inera.intyg.webcert.web.service.monitoring.MonitoringLogService#logIntygRevokeStatusRead}.
     *
     * @param intygsId
     *            ID of the intyg to check revoke status for.
     * @param intygsTyp
     *            Type of the intyg.
     * @param coherentJournaling
     *            true or false.
     * @return
     *         true if the intyg is revoked, false if not.
     */
    boolean isRevoked(String intygsId, String intygsTyp, boolean coherentJournaling);

    /**
     * Gathers the information required to return the list with notifications and creates a list of
     * IntygWithNotificationsResponse objects.
     *
     * @param request
     * @return
     */
    List<IntygWithNotificationsResponse> listCertificatesForCareWithQA(IntygWithNotificationsRequest request);
}
