 /*globals Handlebars*/

 'use strict';

 Handlebars.registerHelper('if_eq', function(a, b, opts) {
     if (a === b) { // Or === depending on your needs
         return opts.fn(this);
     } else {
         return opts.inverse(this);
     }
 });

 Handlebars.registerHelper('atobData', function() {
     return atob(this.data);
 });

 Handlebars.registerHelper('statusAsBoostrap', function() {
     var status = '';
     if (this.result && this.result.status) {
         status = this.result.status;
     } else if (this.status) {
         status = this.status;
     }

     if (status === 'passed') {
         return 'success';
     } else if (status === 'failed') {
         return 'danger';
     } else {
         return 'default';
     }
 });

 function getScenarioResult(steps) {
     if (!steps) {
         return 'pending';
     }
     var scenarioResult = 'passed';
     console.log('steps:' + steps);
     $.each(steps, function(sIndex, step) {
         if (step.result && step.result.status !== 'passed') {
             scenarioResult = step.result.status;
             return false;
         }
     });
     return scenarioResult;
 }

 //4a.function creation
 var slingshot = function(tplId, anchor) {
     $.getJSON('acc_results.json', function(features) {
         var template = $(tplId).html();


         //Loop features
         var featureResult = '';
         console.log('features:' + features);
         $.each(features, function(fIndex, feature) {
             featureResult = 'passed';
             // Loop scenarios
             console.log('elements:' + feature.elements);
             $.each(feature.elements, function(eIndex, scenario) {
                 var scenarioResult = getScenarioResult(scenario.steps);
                 features[fIndex].elements[eIndex].status = scenarioResult;

                 if (scenarioResult !== 'passed') {
                     featureResult = scenarioResult;
                 }
             });

             features[fIndex].status = featureResult;
         });
         var stone = Handlebars.compile(template)(features);
         $(anchor).append(stone);

         $('img').click(function() {
             $(this).toggleClass('bigger');
         });

     });


 };



 //4b.function firing
 slingshot('#tpl', '#anchor'); // since url = 'data.json' , we can use both notations.
