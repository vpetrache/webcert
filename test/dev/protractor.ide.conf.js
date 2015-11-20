// conf.js

/**
 * Setup :
 * npm install
 * <webcert/web/> : ./node_modules/protractor/bin/webdriver-manager update
 *
 * To run tests :
 * <webcert/web/> : grunt e2e
 *
 **/

exports.config = {
    //seleniumAddress: 'http://localhost:4444/wd/hub',
    specs: ['./spec/*.spec.js'],
    baseUrl: 'http://localhost:9088/',
    //rootElement:'html',
    // If chromeOnly is true, we dont need to stand the selenium server.
    // If you want to test with firefox, then set this to false and change the browserName
    chromeOnly: false,

    // Capabilities to be passed to the webdriver instance.
    capabilities: {
        'browserName': 'firefox'//'chrome'
        //'browserName': 'phantomjs'
    },
    rootElement:'html',
    jasmineNodeOpts: {
        onComplete: null,
        isVerbose: true,
        showColors: true,
        includeStackTrace: true
    },
    onPrepare: function() {
        // implicit and page load timeouts
        //browser.manage().timeouts().pageLoadTimeout(40000);
        //browser.manage().timeouts().implicitlyWait(25000);

        // for non-angular page
        /**
         * This makes protractor not wait for Angular promises, such as those from $http or $timeout to resolve,
         * which you might want to do if you're testing behaviour during $http or $timeout (e.g., a 'loading' message),
         * or testing non-Angular sites or pages, such as a separate login page.
         */

        global.testdata = require('../lib/testdata/testdata.js');
        global.pages = require('./../lib/pages.js');
        browser.ignoreSynchronization = false;
    }
};