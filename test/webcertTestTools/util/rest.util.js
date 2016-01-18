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
 * Created by BESA on 2015-11-17.
 */
/*globals*/
'use strict';
var restClient = require('./restclient.util.js');
var env = require('./../environment.js').envConfig;

module.exports = {
    login: function(userJson) {

        // login with doctor Jan Nilsson if noone else is specified
        var user = userJson || {
            'fornamn': 'Jan',
            'efternamn': 'Nilsson',
            'hsaId': 'IFV1239877878-1049',
            'enhetId': 'IFV1239877878-1042',
            'lakare': true,
            'forskrivarKod': '2481632'
        }; 

        var options = {
            url: 'fake',
            method: 'POST',
            body: 'userJsonDisplay=' + JSON.stringify(user)
        };
        return restClient.run(options, 'urlenc');
    },
    createUtkast: function(intygTyp, createJson) {
        var options = {
            url: 'api/utkast/' + intygTyp,
            method: 'POST',
            body: createJson
        };
        return restClient.run(options, 'json');
    },
    deleteAllUtkast: function() {
        var options = {
            url: 'testability/intyg',
            method: 'DELETE'
        };
        return restClient.run(options, 'json');
    },
    deleteUtkast: function(id) {
        var options = {
            url: 'testability/intyg/' + id,
            method: 'DELETE'
        };
        return restClient.run(options, 'json');
    },
    createIntyg: function(createJson) {
        var options = {
            url: 'certificate/',
            method: 'POST',
            body: createJson
        };
        //console.log("================================================CREATEJSON");
        //console.log(createJson);
        return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
    },
    deleteAllIntyg: function() {
        var options = {
            url: 'certificate/',
            method: 'DELETE'
        };
        return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
    },
    deleteIntyg: function(id) {
        var options = {
            url: 'certificate/' + id,
            method: 'DELETE'
        };
        return restClient.run(options, 'json', env.INTYGTJANST_URL + '/resources/');
    }
};