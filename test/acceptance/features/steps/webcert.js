/* global pages, browser, protractor */

'use strict';

var EC = protractor.ExpectedConditions;

var fkUtkastPage = pages.intygpages.fk7263Utkast;

module.exports = function () {

    this.Given(/^fyller i alla nödvändiga fält för intyget$/, function (callback) {
        fkUtkastPage.smittskyddCheckboxClick();
        fkUtkastPage.nedsattMed25CheckboxClick();
        callback();
    });

    this.Given(/^signerar FK7263-intyget$/, function (callback) {

        fkUtkastPage.whenSigneraButtonIsEnabled().then(function () {
            fkUtkastPage.signeraButtonClick();
        });

        browser.getCurrentUrl().then(function (text) {
            global.intyg.id = text.split('/').slice(-1)[0];
            global.intyg.id = global.intyg.id.replace('?signed', '');
        });

        callback();
    });

    // this.Given(/^ska intyget finnas i Mina intyg$/, function (callback) {
    //     // När "Visa intyget"-knappen syns är vi nöjda här.

    //     // TODO / FIXME!

    //     var id = 'viewCertificateBtn-'+ global.intyg.id;
    //     browser.wait(EC.elementToBeClickable(id), 10000);
    //     callback();
    // });


    this.Given(/^jag öppnar intyget$/, function (callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

    this.Given(/^intyget är signerat$/, function (callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

    this.Given(/^intyget är skickat till försäkringskassan$/, function (callback) {
        // Write code here that turns the phrase above into concrete actions
        callback.pending();
    });

    this.Given(/^jag makulerar intyget$/, function (callback) {
        // browser.wait(EC.elementToBeClickable($('#makuleraBtn')), 10000);
        element(by.id('makuleraBtn')).click();
        element(by.id('button1makulera-dialog')).click();
        element(by.id('confirmationOkButton')).click()
        .then(callback);
    });

    this.Given(/^jag raderar utkastet$/, function (callback) {
        // browser.wait(EC.elementToBeClickable($('#makuleraBtn')), 10000);
        element(by.id('ta-bort-utkast')).click();
        element(by.id('confirm-draft-delete')).click();
        element(by.id('confirm-draft-delete-button')).click();
        browser.sleep(2000).then(callback);
    });

    this.Given(/^jag går tillbaka till start$/, function (callback) {
        // browser.wait(EC.elementToBeClickable($('#makuleraBtn')), 10000);
        browser.sleep(2000);
        element(by.id('tillbakaButton ')).click()
        .then(callback);
    });

    this.Given(/^ska intyget visa varningen "([^"]*)"$/, function (arg1, callback) {
        expect(element(by.id('certificate-is-revoked-message-text')).getText())
            .to.eventually.contain(arg1).and.notify(callback);
    });

    //
    //   // Write code here that turns the phrase above into concrete actions
    //   expect(element(by.id('')).getText()).to.eventually.contain(arg1).and.notify(callback);
    //   callback.pending();
    // });

    this.Given(/^ska intyget "([^"]*)" med status "([^"]*)" inte synas mer$/, function (intyg, status, callback) {
      var qaTable = element(by.css('table.table-qa'));

      qaTable.all(by.cssContainingText('tr', intyg)).filter(function(elem, index) {
          return elem.getText().then(function(text) {
              return (text.indexOf(status) > -1);
          });
      }).then(function(filteredElements) {
          // filteredElements[1].element(by.cssContainingText('button', 'Kopiera')).click();
          expect(element(by.cssContainingText('button', 'Kopiera')).isPresent()).to.become(false).and.notify(callback);
          callback();
      });
  });

};
