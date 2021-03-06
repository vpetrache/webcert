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
package se.inera.intyg.webcert.web.service.user;

import se.inera.intyg.infra.security.common.service.Feature;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeature;
import se.inera.intyg.webcert.web.service.user.dto.WebCertUser;

import java.util.List;

public interface WebCertUserService {

     WebCertUser getUser();

    /**
     * Stores (creates or updates) the given key-value pair for current user. Stores in DB and updates the session.
     *
     * @param key
     *         An arbitrary string-based key.
     *
     * @param value
     *         An arbitrary string-based value.
     */
     void storeUserPreference(String key, String value);

    /**
     * Deletes the specified user preference for the current user.
     *
     * @param key
     *      Preference key.
     */
    void deleteUserPreference(String key);

    /**
     * Deletes all user preferences for the current user.
     */
    void deleteUserPreferences();

    void enableFeaturesOnUser(Feature... featuresToEnable);

    void enableModuleFeatureOnUser(String moduleName, ModuleFeature... modulefeaturesToEnable);

    boolean isAuthorizedForUnit(String vardgivarHsaId, String enhetsHsaId, boolean isReadOnlyOperation);

    boolean isAuthorizedForUnit(String enhetsHsaId, boolean isReadOnlyOperation);

    boolean isAuthorizedForUnits(List<String> enhetsHsaIds);

    void updateOrigin(String origin);

    void updateUserRole(String roleName);
}
