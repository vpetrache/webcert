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

/**
 * Created by bennysce on 09/06/15.
 */
'use strict';

var BaseIntyg = require('../../base.intyg.page.js');

var TsDiabetesIntyg = BaseIntyg._extend({
    init: function init() {
        init._super.call(this);
        this.intygType = 'ts-diabetes';

        this.period = element(by.id('observationsperiod'));
        this.insulPeriod = element(by.id('insulinBehandlingsperiod'));
        this.dTyp = element(by.id('diabetestyp'));

        this.kunskapOmAtgarder = element(by.id('kunskapOmAtgarder'));
        this.teckenNedsattHjarnfunktion = element(by.id('teckenNedsattHjarnfunktion'));
        this.saknarFormagaKannaVarningstecken = element(by.id('saknarFormagaKannaVarningstecken'));
        this.allvarligForekomst = element(by.id('allvarligForekomst'));
        this.allvarligForekomstBeskrivning = element(by.id('allvarligForekomstBeskrivning'));
        this.allvarligForekomstTrafiken = element(by.id('allvarligForekomstTrafiken'));
        this.allvarligForekomstTrafikenBeskrivning = element(by.id('allvarligForekomstTrafikBeskrivning'));
        this.egenkontrollBlodsocker = element(by.id('egenkontrollBlodsocker'));
        this.allvarligForekomstVakenTid = element(by.id('allvarligForekomstVakenTid'));
        this.vakenTidObservationsTid = element(by.id('allvarligForekomstVakenTidObservationstid'));

        this.synIntyg = element(by.id('separatOgonlakarintyg'));

        this.falt1 = {
            bedomningKanInteTaStallning: element(by.id('bedomningKanInteTaStallning')),
            bedomning: element(by.id('bedomning')),
            endastKost: element(by.id('endastKost')),
            tabletter: element(by.id('tabletter')),
            insulin: element(by.id('insulin')),
            annanBehandling: element(by.id('annanBehandlingBeskrivning'))
        };

        this.kommentar = element(by.id('kommentar'));
        this.specKomp = element(by.id('lakareSpecialKompetens'));

        this.intygetAvser = element(by.id('intygAvser'));
        this.idStarktGenom = element(by.id('identitet'));
    },
    get: function get(intygId) {
        get._super.call(this, intygId);
    }
});

module.exports = new TsDiabetesIntyg();
