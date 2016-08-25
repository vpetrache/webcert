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
 * Created by BESA on 2015-11-25.
 */
'use strict';

var restUtil = require('./../util/rest.util.js');
var arendeFromJsonFactory = require('./../util/arendeFromJsonFactory.js');

function createArende(createJson) {
    restUtil.login();
    return restUtil.createArende(createJson);
}

module.exports = {

    // Intyg services
    deleteAllIntyg: function() {
        restUtil.login();
        return restUtil.deleteAllIntyg();
    },
    deleteIntyg: function(id) {
        restUtil.login();
        return restUtil.deleteIntyg(id);
    },
    createWebcertIntyg: function(createJson) {
        restUtil.login();
        return restUtil.createWebcertIntyg(createJson);
    },
    createUtkast: function(intygType, template) {
        restUtil.login();

        var utkastTemplate = template;
        if (typeof template === 'undefined') {
            utkastTemplate = {
                'intygType': intygType,
                'patientPersonnummer': '19121212-1212',
                'patientFornamn': 'Tolvan',
                'patientEfternamn': 'Tolvansson',
                'patientPostadress': 'Svensson, Storgatan 1, PL 1234',
                'patientPostnummer': '12345',
                'patientPostort': 'Småmåla'
            };
        }
        return restUtil.createUtkast(intygType, utkastTemplate);
    },
    saveUtkast: function(intygType, intygId, version, utkastJson) {
        return restUtil.saveUtkast(intygType, intygId, version, utkastJson);
    },
    deleteAllUtkast: function() {
        restUtil.login();
        return restUtil.deleteAllUtkast();
    },
    deleteUtkast: function(id) {
        restUtil.login();
        return restUtil.deleteUtkast(id);
    },

    // Ärenden
    createArende: createArende,
    createArendeFromTemplate: function(intygType, intygId, meddelandeId, meddelande, amne, status, komplettering, svarPa) {
    	var arende = arendeFromJsonFactory.get({
    	    intygType:intygType,
            intygId:intygId,
            meddelandeId:meddelandeId,
            meddelande:'Hur är det med arbetstiden?',
            amne:amne,
            status:status,
            kompletteringar:komplettering
    	});

        if(svarPa){
            arende.svarPa = svarPa;
        }
        
    	console.log('arende to be created: '+JSON.stringify(arende));
    	createArende(arende).then(function(response) {
            console.log('response code:' + response.statusCode);
        });
    },
    deleteAllArenden: function() {
    	console.log('deleting all arenden');
        restUtil.login();
        return restUtil.deleteAllArenden();
    },
    deleteArende: function(id) {
        restUtil.login();
        return restUtil.deleteArende(id);
    }
};
