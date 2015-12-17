/* globals browser, intyg, logg */

'use strict';

module.exports = function() {
    this.Given(/^jag går till Mina intyg för patienten "([^"]*)"$/, function(pnr, callback) {
        browser.ignoreSynchronization = true;
        browser.get(process.env.MINAINTYG_URL + '/welcome.jsp');
        element(by.id('guid')).sendKeys(pnr);
        element(by.css('input.btn')).click().then(function() {

            // Detta behövs pga att Mina intyg är en extern sida
            browser.sleep(2000);

            // Om samtyckesruta visas
            element(by.id('consentTerms')).isPresent().then(function(result) {
                if (result) {
                    logg('Lämnar samtycke..');
                    element(by.id('giveConsentButton')).click().then(callback);
                } else {
                    callback();
                }
            });
        });
    });

    this.Given(/^ska intygets status i Mina intyg visa "([^"]*)"$/, function(status, callback) {
        var intygElement = element(by.id('certificate-' + intyg.id));
        expect(intygElement.getText()).to.eventually.contain(status).and.notify(callback);
    });
};