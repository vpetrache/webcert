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

/* globals pages, logger, protractor, browser*/

'use strict';

var sokSkrivIntygPage = pages.sokSkrivIntyg.pickPatient;

module.exports = function() {
    this.Given(/^ska jag( inte)? se en varning om kakor$/, function(inte, callback) {
        var shouldBeVisible = (inte === undefined); //Om 'inte' finns med i stegnamnet
        console.log('shouldBeVisible:' + shouldBeVisible);
        expect(sokSkrivIntygPage.cookie.consentBanner.isDisplayed()).to.eventually.equal(shouldBeVisible).then(function() {
            if (!inte) {
                inte = '';
            }
            logger.info('OK - Varning syns' + inte);
        }, function(reason) {
            callback('FEL : ' + reason);
        }).then(callback);
    });

    this.Given(/^jag accepterar kakor$/, function(callback) {
        sokSkrivIntygPage.cookie.consentBtn.sendKeys(protractor.Key.SPACE)
            .then(callback());
    });

    this.Given(/^laddar om sidan$/, function(callback) {
        browser.refresh().then(callback());
    });

};
