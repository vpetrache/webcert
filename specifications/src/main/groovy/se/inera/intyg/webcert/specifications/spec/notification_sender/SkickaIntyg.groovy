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

package se.inera.intyg.webcert.specifications.spec.notification_sender

import se.inera.intyg.webcert.specifications.spec.util.WebcertRestUtils;
import se.inera.intyg.webcert.specifications.spec.util.RestClientFixture
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.URLENC
import static se.inera.intyg.webcert.specifications.spec.util.WebcertRestUtils.*

class SkickaIntyg {

    String intygId
    String intygTyp
    String mottagarId = "FK"

    def response

    public void execute() {
        WebcertRestUtils.login()
        response = WebcertRestUtils.sendIntyg(intygTyp, intygId, mottagarId)
    }

    public boolean intygSkickat() {
        response.success
    }
}
