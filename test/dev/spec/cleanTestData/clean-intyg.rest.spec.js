'use strict';

var testdataHelper = wcTestTools.helpers.testdata;

describe('Clean intyg via rest', function() {

    it('should login rest client and clean all intyg', function() {
        testdataHelper.deleteAllIntyg();
    });
});