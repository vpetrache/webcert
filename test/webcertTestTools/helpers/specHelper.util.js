/**
 * Created by BESA on 2015-11-25.
 * Holds helper functions for actions that are needed often in specs.
 */
/*globals browser */
'use strict';

var pages = require('./../pages.js');
var WelcomePage = pages.welcome;
var SokSkrivIntygPage = pages.app.views.sokSkrivIntyg;

module.exports = {
    login: function(userOptional) {
        WelcomePage.get();
        WelcomePage.login(userOptional || 'IFV1239877878-104B_IFV1239877878-1042');
        browser.sleep(500); // need to sleep here since we aren't in the angular app yet
        expect(SokSkrivIntygPage.getDoctorText()).toContain('Åsa Andersson');
    },
    createUtkastForPatient: function(patientId, intygType) {
        SokSkrivIntygPage.selectPersonnummer(patientId);
        SokSkrivIntygPage.selectIntygType('string:'+ intygType);
        SokSkrivIntygPage.continueToUtkast();
        var UtkastPage = pages.intygpages[intygType+'Utkast'];
        expect(UtkastPage.isAt()).toBe(true);
    },
    generateTestGuid: function(){
        function s4() {
            return Math.floor((1 + Math.random()) * 0x10000)
                .toString(16)
                .substring(1);
        }
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
            s4() + '-' + s4() + s4() + s4();
    },
    loadHttpBackendMock: function() {
        browser.addMockModule('httpBackendMock', function () {
            function loadScript(urls) {
                for (var i = 0; i < urls.length; i++) {
                    var newScript = document.createElement('script');
                    newScript.type = 'text/javascript';
                    newScript.src = urls[i];
                    newScript.async = true;
                    newScript.defer = false;

                    document.getElementsByTagName('head')[0].appendChild(newScript);
                }
            }

            loadScript([
                '/web/webjars/angularjs/1.4.7/angular-mocks.js'
            ]);

            angular.module('httpBackendMock', ['ngMockE2E']);
        });
    }
};