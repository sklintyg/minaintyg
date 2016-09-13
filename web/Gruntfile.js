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

    grunt.loadNpmTasks('grunt-connect-proxy');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-csslint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-angular-templates');

    var SRC_DIR = 'src/main/webapp/app/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = 'build/apps/app/';

    var minaintyg = grunt.file.expand({cwd:SRC_DIR}, ['**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/app.js']).sort();
    grunt.file.write(DEST_DIR + 'app-deps.json', JSON.stringify(minaintyg.
        map(function(file){ return '/app/'+file; }).
        concat('/app/templates.js'), null, 4));

    var minaintygBase = [SRC_DIR + 'base/app.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));
    minaintyg = [SRC_DIR + 'app.js', DEST_DIR + 'templates.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));

    var modules = {
        'common':      { base: 'common/web' },
        'fk7263':      { base: 'intygstyper/fk7263' },
        'ts-bas':      { base: 'intygstyper/ts/ts-bas' },
        'ts-diabetes': { base: 'intygstyper/ts/ts-diabetes' },
        'luse':        { base: 'intygstyper/fk/luse', angularModule:'luse' },
        'lisu':        { base: 'intygstyper/fk/lisu', angularModule:'lisu' },
        'luae_na':     { base: 'intygstyper/fk/luae_na', angularModule:'luae_na' },
        'luae_fs':     { base: 'intygstyper/fk/luae_fs', angularModule:'luae_fs' }
    };
    Object.keys(modules).forEach(function(moduleName) {
        var module = modules[moduleName];
        module.name = moduleName;
        if (!module.angularModule) {
            module.angularModule = moduleName;
        }
        module.src = '/../../'+module.base+'/src/main/resources/META-INF/resources/webjars/' + moduleName + '/minaintyg';
        module.dest = '/../../'+module.base+'/target/classes/META-INF/resources/webjars/' + moduleName + '/minaintyg';
    });

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
                    ignores: ['**/*.min.js', '**/vendor/**']
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
                dest: __dirname + '/build/apps/app/templates.js',
                options: {
                    module: 'minaintyg',
                    url: function(url) {
                        return '/' + url.replace('../', '/');
                    }
                }
            }
        },

        connect: {
            server: {
                options: {
                    port: 8089,
                    base: 'src/main/webapp',
                    hostname: '*',
                    keepalive: true,
                    middleware: function(connect/*, options*/) {
                        var proxy = require('grunt-connect-proxy/lib/utils').proxyRequest;
                        var middlewares = [];
                        middlewares.push(
                            connect().use(
                                '/web',
                                connect.static(__dirname + '/src/main/webapp') // jshint ignore:line
                            ));
                        middlewares.push(
                            connect().use(
                                '/app',
                                connect.static(__dirname + '/src/main/webapp/app') // jshint ignore:line
                            ));
                        middlewares.push(
                            connect().use(
                                '/app/app-deps.js',
                                connect.static(__dirname + DEST_DIR + '/app-deps.js') // jshint ignore:line
                            ));
                        Object.keys(modules).forEach(function(moduleName) {
                            var module = modules[moduleName];
                            middlewares.push(
                                connect().use(
                                        '/web/webjars/'+module.name+'/minaintyg',
                                    connect.static(__dirname + module.src) //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                        '/web/webjars/'+module.name+'/minaintyg/templates.js',
                                    connect.static(__dirname + module.dest + '/templates.js') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                        '/web/webjars/'+module.name+'/minaintyg/module-deps.json',
                                    connect.static(__dirname + module.dest + '/module-deps.json') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                        '/web/webjars/'+module.name+'/minaintyg/css',
                                    connect.static(__dirname + module.dest + '/css')//jshint ignore:line
                                ));
                        });
                        middlewares.push(proxy);
                        return middlewares;
                    }
                },
                proxies: [
                    {
                        context: '/',
                        host: 'localhost',
                        port: 8088
                    }
                ]
            }
        }
    });

    grunt.registerTask('default', ['ngtemplates:minaintyg', 'concat', 'ngAnnotate', 'uglify', 'jshint' ]);
    grunt.registerTask('lint', [ 'jshint', 'csslint' ]);
    grunt.registerTask('test', [ 'karma' ]);
    grunt.registerTask('server', [ 'configureProxies:server', 'connect:server' ]);
};
