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

/* global module */
require('path');

module.exports = function(grunt) {
    'use strict';

    grunt.loadNpmTasks('grunt-protractor-runner');
    grunt.loadNpmTasks('grunt-protractor-webdriver');
    grunt.loadNpmTasks('grunt-env');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks("grunt-jsbeautifier");

    var devSuite = grunt.option('suite') || 'app';
    grunt.initConfig({
        jshint: {
            acc: [
                'acceptance/features/steps/*.js',
                'acceptance/features/steps/**/*.js' //,
                //'webcertTestTools/**/*.js',
                //'webcertTestTools/*.js'
            ],
            options: {
                force: false,
                jshintrc: '../../common/build-tools/src/main/resources/jshint/.jshintrc'
            }
        },
        jsbeautifier: {
            verify: {
                src: [
                    'acceptance/features/steps/*.js',
                    'acceptance/features/steps/**/*.js' //,
                    //'webcertTestTools/**/*.js',
                    //'webcertTestTools/*.js'
                ],
                options: {
                    mode: 'VERIFY_ONLY',
                    config: '.jsbeautifyrc'
                }
            },
            modify: {
                src: [
                    'acceptance/features/steps/*.js',
                    'acceptance/features/steps/**/*.js',
                    'webcertTestTools/**/*.js',
                    'webcertTestTools/*.js'
                ],
                options: {
                    mode: 'VERIFY_AND_WRITE',
                    config: '.jsbeautifyrc'
                }
            }
        },
        env: grunt.file.readJSON('./webcertTestTools/envConfig.json'),
        protractor: {
            options: {
                //configFile: './protractor.cli.conf.js', // Target-specific config file
                keepAlive: false, // If false, the grunt process stops when the test fails.
                noColor: false, // If true, protractor will not use colors in its output.
                args: {
                    // Arguments passed to the command
                }
            },

            // Grunt requires at least one target to run so you can simply put 'all: {}' here too.
            dev: {
                options: {
                    configFile: './dev/protractor.conf.js',
                    args: {
                        'suite': devSuite
                    } // Target-specific arguments
                }
            },
            acc: {
                options: {
                    configFile: './acceptance/protractor-conf.js',
                    args: {}
                }
            }
        },

        protractor_webdriver: { // jshint ignore:line
            options: {
                // Task-specific options go here.
            }
        }

    });

    grunt.registerTask('default', function(environment, tags) {
        if (!environment) {
            var defaultEnv = 'dev';
            grunt.log.subhead('Ingen miljö vald, använder ' + defaultEnv + '-miljön..');
            environment = defaultEnv;
        }
        grunt.task.run(['env:' + environment, 'protractor_webdriver', 'protractor:dev']);
    });


    // Run: 'grunt acc:ip20'
    grunt.task.registerTask('acc', 'Task för att köra acceptanstest', function(environment) {

        //Miljö
        if (!environment) {
            var defaultEnv = 'ip30';
            grunt.log.subhead('Ingen miljö vald, använder ' + defaultEnv + '-miljön..');
            environment = defaultEnv;
        }

        // Ange taggar som grunt.option istället for argument till task. Flexiblare när det gäller att
        // kombinera OCH och ELLER operatorer.
        // https://github.com/cucumber/cucumber/wiki/Tags
        var tagsArray = ['~@notReady', '~@waitingForFix', '~@lisu'];
        if (grunt.option('tags')) {
            tagsArray = grunt.option('tags').split(',');
            tagsArray.forEach(function(tag, index) {
                tagsArray[index] = tagsArray[index].replace(' ', ',');
            });
        }
        grunt.log.subhead('Taggar:' + tagsArray);
        grunt.config.set('protractor.acc.options.args.cucumberOpts.tags', tagsArray);


        //Tasks
        var tasks = [];
        if (!grunt.option('CI')) {
            tasks = ['jshint:acc', 'jsbeautifier:verify'];
        }

        tasks.push('env:' + environment);
        tasks.push('protractor_webdriver');
        tasks.push('protractor:acc');

        grunt.task.run(tasks);

    });
};
