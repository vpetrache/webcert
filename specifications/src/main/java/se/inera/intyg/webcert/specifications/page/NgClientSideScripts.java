/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.webcert.specifications.page;

/**
 * Javascript scripts to perform against AngularJs sites when using Selenium
 * (copied from @link https://github.com/bbaia/protractor-net/blob/master/src/Protractor/ClientSideScripts.cs
 * and updated using @link https://github.com/angular/protractor/blob/master/lib/clientsidescripts.js).
 */
public final class NgClientSideScripts {

    private NgClientSideScripts() { }

    /**
     * Tests whether angular.getTestability is present on a page. Retries
     * in case the page is just loading slowly.
     *
     * Asynchronous.
     *
     * @param {string} The selector housing an ng-app
     * @param {number} attempts Number of times to retry.
     * @param {function} asyncCallback callback
     */
    public static final String TEST_FOR_ANGULAR_TESTABILITY =
            "var asyncCallback = arguments[2];\n"
                    + "var rootSelector = arguments[0];\n"
                    + "var attempts = arguments[1];\n"
                    + "var el = document.querySelector(rootSelector);\n"
                    + "  var callback = function(args) {\n"
                    + "    setTimeout(function() {\n"
                    + "      asyncCallback(args);\n"
                    + "    }, 0);\n"
                    + "  };\n"
                    + "  var check = function(n) {\n"
                    + "    var exception = null;\n"
                    + "    try {\n"
                    + "      if (window.angular && window.angular.getTestability && window.angular.getTestability(el)) {\n"
                    + "        callback([true, null]);\n"
                    + "        return;"
                    + "      }\n"
                    + "    } catch (e) {\n"
                    + "      exception = e;\n"
                    + "    }\n"
                    + "    if (n < 1) {\n"
                    + "      if (exception) {\n"
                    + "        callback([false, exception]);\n"
                    + "      } else if (window.angular) {\n"
                    + "        callback([false, 'angular never provided getTestability']);\n"
                    + "      } else {\n"
                    + "        callback([false, 'retries looking for angular exceeded']);\n"
                    + "      }\n"
                    + "    } else {\n"
                    + "      window.setTimeout(function() {check(n - 1);}, 500);\n"
                    + "    }\n"
                    + "  };\n"
                    + "  check(attempts);";

    /**
     * Waits (asynchronously) until Angular has finished rendering and has
     * no outstanding $http calls before continuing.
     *
     * arguments[0] {string} The selector housing an ng-app
     * arguments[1] {function} callback
     */
    public static final String WAIT_FOR_ANGULAR =
            "var rootSelector = arguments[0];\n"
                    + "var el = document.querySelector(rootSelector);\n"
                    + "\n"
                    + "  try {\n"
                    + "    if (!window.angular) {\n"
                    + "      throw new Error('angular could not be found on the window');\n"
                    + "    }\n"
                    + "    if (angular.getTestability) {\n"
                    + "      angular.getTestability(el).whenStable(callback);\n"
                    + "    } else {\n"
                    + "      if (!angular.element(el).injector()) {\n"
                    + "        throw new Error('root element (' + rootSelector + ') has no injector.' +\n"
                    + "           ' this may mean it is not inside ng-app.');\n"
                    + "      }\n"
                    + "      angular.element(el).injector().get('$browser').\n"
                    + "          notifyWhenNoOutstandingRequests(callback);\n"
                    + "    }\n"
                    + "  } catch (err) {\n"
                    + "    callback(err.message);\n"
                    + "  }";

    /**
     * Tests whether the angular global variable is present on a page. Retries
     * in case the page is just loading slowly.
     *
     * Asynchronous.
     *
     * @param {number} attempts Number of times to retry.
     * @param {function} asyncCallback callback
     */
    public static final String TEST_FOR_ANGULAR =
            "var asyncCallback = arguments[1];\n"
                    + "var attempts = arguments[0];\n"
                    + "  var callback = function(args) {\n"
                    + "    setTimeout(function() {\n"
                    + "      asyncCallback(args);\n"
                    + "    }, 0);\n"
                    + "  };\n"
                    + "  var check = function(n) {\n"
                    + "    try {\n"
                    + "      if (window.angular && window.angular.resumeBootstrap) {\n"
                    + "        callback([true, null]);\n"
                    + "      } else if (n < 1) {\n"
                    + "        if (window.angular) {\n"
                    + "          callback([false, 'angular never provided resumeBootstrap']);\n"
                    + "        } else {\n"
                    + "          callback([false, 'retries looking for angular exceeded']);\n"
                    + "        }\n"
                    + "      } else {\n"
                    + "        window.setTimeout(function() {check(n - 1);}, 1000);\n"
                    + "      }\n"
                    + "    } catch (e) {\n"
                    + "      callback([false, e]);\n"
                    + "    }\n"
                    + "  };\n"
                    + "  check(attempts);";

    /**
     * Continue to bootstrap Angular.
     *
     * arguments[0] {array} The module names to load.
     */
    public static final String RESUME_ANGULAR_BOOTSTRAP =
            "angular.resumeBootstrap(arguments[0].length ? arguments[0].split(',') : []);";

    /**
     * Return the current url using $location.absUrl().
     *
     * arguments[0] {string} The selector housing an ng-app
     */
    public static final String GET_LOCATION_ABS_URL =
            "var selector = arguments[0];\n"
                    + "  var el = document.querySelector(selector);\n"
                    + "  if (angular.getTestability) {\n"
                    + "    return angular.getTestability(el).\n"
                    + "        getLocation();\n"
                    + "  }\n"
                    + "  return angular.element(el).injector().get('$location').absUrl();\n";

    /**
     * EVALUATE an Angular expression in the context of a given element.
     *
     * arguments[0] {Element} The element in whose scope to evaluate.
     * arguments[1] {string} The expression to evaluate.
     *
     * @return {?Object} The result of the evaluation.
     */
    public static final String EVALUATE =
            "var element = arguments[1];\n"
                    + "var expression = arguments[0];\n"
                    + "return angular.element(element).scope().$eval(expression);";

    /**
     * Find a list of elements in the page by their angular binding.
     *
     * @param {string} binding The binding, e.g. {{cat.name}}.
     * @param {boolean} exactMatch Whether the binding needs to be matched exactly
     * @param {Element} using The scope of the search.
     * @param {string} rootSelector The selector to use for the root app element.
     *
     * @return {Array.<Element>} The elements containing the binding.
     */
    public static final String FIND_BINDINGS =
            "var rootSelector = arguments[3];\n"
                    + "var using = arguments[2];\n"
                    + "var exactMatch = arguments[1];\n"
                    + "var binding = arguments[0];\n"
                    + "var root = document.querySelector(rootSelector || 'body');\n"
                    + "using = using || document;\n"
                    + "if (angular.getTestability) {\n"
                    + "    return angular.getTestability(root).\n"
                    + "        findBindings(using, binding, exactMatch);\n"
                    + "}\n"
                    + "var bindings = using.getElementsByClassName('ng-binding');\n"
                    + "var matches = [];\n"
                    + "for (var i = 0; i < bindings.length; ++i) {\n"
                    + "    var dataBinding = angular.element(bindings[i]).data('$binding');\n"
                    + "    if (dataBinding) {\n"
                    + "      var bindingName = dataBinding.exp || dataBinding[0].exp || dataBinding;\n"
                    + "      if (exactMatch) {\n"
                    + "        var matcher = new RegExp('({|\\\\s|^|\\\\|)' + binding + '(}|\\\\s|$|\\\\|)');\n"
                    + "        if (matcher.test(bindingName)) {\n"
                    + "          matches.push(bindings[i]);\n"
                    + "        }\n"
                    + "      } else {\n"
                    + "        if (bindingName.indexOf(binding) != -1) {\n"
                    + "          matches.push(bindings[i]);\n"
                    + "        }\n"
                    + "      }\n"
                    + "    }\n"
                    + "}\n"
                    + "return matches; /* Return the whole array for webdriver.findElements. */\n";

    /**
     * Find select elements by model name.
     *
     * arguments[0] {Element} The scope of the search.
     * arguments[1] {string} The model name.
     *
     * @return {Array.WebElement} The matching select elements.
     */
    public static final String FIND_SELECTS =
            "var using = arguments[1] || document;\n"
                    + "var model = arguments[0];\n"
                    + "var prefixes = ['ng-', 'ng_', 'data-ng-', 'x-ng-', 'ng\\\\:'];\n"
                    + "for (var p = 0; p < prefixes.length; ++p) {\n"
                    + "    var selector = 'select[' + prefixes[p] + 'model=\"' + model + '\"]';\n"
                    + "    var inputs = using.querySelectorAll(selector);\n"
                    + "    if (inputs.length) {\n"
                    + "        return inputs;\n"
                    + "    }\n"
                    + "}";

    /**
     * Find elements by model name.
     *
     * @param {string} model The model name.
     * @param {Element} using The scope of the search.
     * @param {string} rootSelector The selector to use for the root app element.
     *
     * @return {Array.<Element>} The matching elements.
     */
    public static final String FIND_ELEMENTS =
            "var using = arguments[1] || document;\n"
                    + "var model = arguments[0];\n"
                    + "var rootSelector = arguments[2];\n"
                    + "var root = document.querySelector(rootSelector || 'body');\n"
                    + "  using = using || document;\n"
                    + "\n"
                    + "  if (angular.getTestability) {\n"
                    + "    return angular.getTestability(root).\n"
                    + "        findModels(using, model, true);\n"
                    + "  }\n"
                    + "  var prefixes = ['ng-', 'ng_', 'data-ng-', 'x-ng-', 'ng\\\\:'];\n"
                    + "  for (var p = 0; p < prefixes.length; ++p) {\n"
                    + "    var selector = '[' + prefixes[p] + 'model=\"' + model + '\"]';\n"
                    + "    var elements = using.querySelectorAll(selector);\n"
                    + "    if (elements.length) {\n"
                    + "      return elements;\n"
                    + "    }\n"
                    + "  }";

    /**
     * Find all rows of an ng-repeat.
     *
     * @param {string} repeater The text of the repeater, e.g. 'cat in cats'.
     * @param {boolean} exact Whether the repeater needs to be matched exactly
     * @param {Element} using The scope of the search.
     *
     * @return {Array.<Element>} All rows of the repeater.
     */
    public static final String FIND_ALL_REPEATER_ROWS =
            "var using = arguments[2] || document;\n"
                    + "var exact = arguments[1];\n"
                    + "var repeater = arguments[0];\n"
                    + "  function repeaterMatch(ngRepeat, repeater, exact) {\n"
                    + "    if (exact) {\n"
                    + "      return ngRepeat.split(' track by ')[0].split(' as ')[0].split('|')[0].\n"
                    + "          trim() == repeater;\n"
                    + "    } else {\n"
                    + "      return ngRepeat.indexOf(repeater) != -1;\n"
                    + "    }\n"
                    + "  }\n"
                    + "\n"
                    + "  using = using || document;\n"
                    + "\n"
                    + "  var rows = [];\n"
                    + "  var prefixes = ['ng-', 'ng_', 'data-ng-', 'x-ng-', 'ng\\\\:'];\n"
                    + "  for (var p = 0; p < prefixes.length; ++p) {\n"
                    + "    var attr = prefixes[p] + 'repeat';\n"
                    + "    var repeatElems = using.querySelectorAll('[' + attr + ']');\n"
                    + "    attr = attr.replace(/\\\\/g, '');\n"
                    + "    for (var i = 0; i < repeatElems.length; ++i) {\n"
                    + "      if (repeaterMatch(repeatElems[i].getAttribute(attr), repeater, exact)) {\n"
                    + "        rows.push(repeatElems[i]);\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "  for (var p = 0; p < prefixes.length; ++p) {\n"
                    + "    var attr = prefixes[p] + 'repeat-start';\n"
                    + "    var repeatElems = using.querySelectorAll('[' + attr + ']');\n"
                    + "    attr = attr.replace(/\\\\/g, '');\n"
                    + "    for (var i = 0; i < repeatElems.length; ++i) {\n"
                    + "      if (repeaterMatch(repeatElems[i].getAttribute(attr), repeater, exact)) {\n"
                    + "        var elem = repeatElems[i];\n"
                    + "        while (elem.nodeType != 8 ||\n"
                    + "            !repeaterMatch(elem.nodeValue, repeater, exact)) {\n"
                    + "          if (elem.nodeType == 1) {\n"
                    + "            rows.push(elem);\n"
                    + "          }\n"
                    + "          elem = elem.nextSibling;\n"
                    + "        }\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "  return rows;";

}
