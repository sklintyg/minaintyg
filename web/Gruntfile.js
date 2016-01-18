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
module.exports = function(grunt) {
    'use strict';

    grunt.loadNpmTasks('grunt-contrib-csslint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-angular-templates');

    var SRC_DIR = 'src/main/webapp/app/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = 'target/webapp/app/';

    var minaintyg = grunt.file.readJSON(SRC_DIR + 'app-deps.json').map(function(file) {
        return file.replace(/\/app\//g, SRC_DIR);
    });

    var minaintygBase = [SRC_DIR + 'base/app.js'].concat(minaintyg);
    minaintyg = [SRC_DIR + 'app.js', DEST_DIR + 'templates.js'].concat(minaintyg);

    grunt.initConfig({

        csslint: {
            minaintyg: {
                options: {
                    csslintrc: 'target/build-tools/csslint/.csslintrc',
                    force: true
                },
                src: [ SRC_DIR + '../**/*.css' ]
            }
        },

        concat: {
            minaintyg: {
                src: minaintyg,
                dest: DEST_DIR + 'app.min.js'
            },
            minaintygBase: {
                src: minaintygBase,
                dest: DEST_DIR + 'base/app.min.js'
            }
        },

        jshint: {
            minaintyg: {
                options: {
                    jshintrc: 'target/build-tools/jshint/.jshintrc',
                    force: false,
                    ignores: ['**/*.min.js', '**/vendor/*.js']
                },
                src: [ 'Gruntfile.js', SRC_DIR + '**/*.js', TEST_DIR + '**/*.js', '!' + DEST_DIR + '/app.min.js',
                        '!' + DEST_DIR + '/base/app.min.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: 'src/main/resources/karma.conf.ci.js',
                reporters: [ 'mocha' ]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'app.min.js',
                dest: DEST_DIR + 'app.min.js'
            },
            minaintygBase: {
                src: DEST_DIR + 'base/app.min.js',
                dest: DEST_DIR + 'base/app.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'app.min.js',
                dest: DEST_DIR + 'app.min.js'
            },
            minaintygBase: {
                src: DEST_DIR + 'base/app.min.js',
                dest: DEST_DIR + 'base/app.min.js'
            }
        },

        ngtemplates : {
            minaintyg: {
                cwd: __dirname + '/src/main/webapp',
                src: ['app/**/*.html'],
                dest: __dirname + '/target/webapp/app/templates.js',
                options: {
                    module: 'minaintyg',
                    url: function(url) {
                        return '/' + url.replace('../', '/');
                    }
                }
            }
        }
    });

    grunt.registerTask('default', ['ngtemplates:minaintyg', 'concat', 'ngAnnotate', 'uglify' ]);
    grunt.registerTask('lint', [ 'jshint', 'csslint' ]);
    grunt.registerTask('test', [ 'karma' ]);
};
