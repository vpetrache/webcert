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
package se.inera.intyg.webcert.web.web.controller.integration;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import se.inera.intyg.infra.security.authorities.validation.AuthoritiesValidator;
import se.inera.intyg.infra.security.common.model.UserOriginType;
import se.inera.intyg.webcert.web.service.user.WebCertUserService;

/**
 * Base class for deep-integration and uthopp controllers.
 *
 * Created by eriklupander on 2015-10-08.
 */
public abstract class BaseIntegrationController {

    protected WebCertUserService webCertUserService;
    private String urlBaseTemplate;
    protected AuthoritiesValidator authoritiesValidator = new AuthoritiesValidator();

    // ~ API
    // ========================================================================================

    public void validateRedirectToIntyg(String intygId) {

        // Input validation
        if (Strings.nullToEmpty(intygId).trim().isEmpty()) {
            throw new IllegalArgumentException("Path parameter 'intygId' was either whitespace, empty (\"\") or null");
        }

        // Do Auth validation, given the subclass role/origin constraints
        authoritiesValidator.given(webCertUserService.getUser())
                .roles(getGrantedRoles())
                .origins(getGrantedRequestOrigin())
                .orThrow();

    }

    @Autowired
    public void setUrlBaseTemplate(String urlBaseTemplate) {
        this.urlBaseTemplate = urlBaseTemplate;
    }

    @Autowired
    public void setWebCertUserService(WebCertUserService webCertUserService) {
        this.webCertUserService = webCertUserService;
    }

    // ~ Protected
    // ========================================================================================

    /**
     * Method should return the granted roles that allows.
     *
     * @return
     */
    protected abstract String[] getGrantedRoles();

    protected abstract UserOriginType getGrantedRequestOrigin();

    protected String getUrlBaseTemplate() {
        return urlBaseTemplate;
    }

    protected WebCertUserService getWebCertUserService() {
        return webCertUserService;
    }

}
