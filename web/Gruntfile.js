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

    require('time-grunt')(grunt);
    require('jit-grunt')(grunt, {
        bower: 'grunt-bower-task',
        configureProxies: 'grunt-connect-proxy',
        ngtemplates: 'grunt-angular-templates'
    });

    var SRC_DIR = 'src/main/webapp/app/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = (grunt.option('outputDir') || 'build/webapp/') +  'app/';
    var TEST_OUTPUT_DIR = (grunt.option('outputDir') || 'build/karma/');
    var SKIP_COVERAGE = grunt.option('skip-coverage') !== undefined ? grunt.option('skip-coverage') : true;

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
        module.src = '/../../' + module.base + '/src/main/resources/META-INF/resources/webjars/' + moduleName + '/minaintyg';
        module.dest = '/../../' + module.base + '/build/resources/main/META-INF/resources/webjars/' + moduleName + '/minaintyg';
    });

    function buildListForAllModules(callback) {
        var list = [];
        Object.keys(modules).forEach(function(moduleName) {
            var module = modules[moduleName];
            list.push(callback(module));
        });
        return list;
    }

    function buildObjectForAllModules(callback) {
        var obj = {};
        Object.keys(modules).forEach(function(moduleName) {
            var module = modules[moduleName];
            obj[module.name] = callback(module);
        });
        return obj;
    }

    grunt.registerTask('generateModuleDeps', function() {
        // Generate minaintyg app-deps.json
        var files = grunt.file.expand({cwd: SRC_DIR},
            ['**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/app.js']).sort();
        grunt.file.write(DEST_DIR + 'app-deps.json', JSON.stringify(files.
        map(function(file) {
            return '/app/' + file;
        }).
        concat('/app/templates.js'), null, 4));

        // Generate all module-deps.json
        Object.keys(modules).forEach(function(moduleName) {
            var module = modules[moduleName];
            var files = grunt.file.expand({cwd: __dirname + module.src},
                ['**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/module.js']).sort();
            grunt.file.write(__dirname + module.dest + '/module-deps.json', JSON.stringify(files.
            map(function(file) {
                return '/web/webjars/' + module.name + '/minaintyg/' + file;
            }).
            concat('/web/webjars/' + module.name + '/minaintyg/templates.js'), null, 4));
        });
    });

    grunt.initConfig({

        bower: {
            install: {
                options: {
                    copy: false
                }
            }
        },

        wiredep: {
            minaintyg: {
                directory: 'src/main/webapp/bower_components',
                src: [
                    SRC_DIR + '../pubapp/**/index.html',
                    SRC_DIR + '../**/*.jsp',
                    'karma.conf.js'
                ],
                ignorePath: '../..',
                fileTypes: {
                    jsp: {
                        block: /(([ \t]*)<!--\s*bower:*(\S*)\s*-->)(\n|\r|.)*?(<!--\s*endbower\s*-->)/gi,
                        detect: {
                            js: /<script.*src=['"]([^'"]+)/gi,
                            css: /<link.*href=['"]([^'"]+)/gi
                        },
                        replace: {
                            js: function(filePath) {
                                if (filePath[0] !== '/') {
                                    filePath = '/' + filePath;
                                }
                                return '<script type="text/javascript" src="'+filePath+'"></script>';
                            },
                            css: function(filePath) {
                                if (filePath[0] !== '/') {
                                    filePath = '/' + filePath;
                                }
                                return '<link rel="stylesheet" href="'+filePath+'" />';
                            }
                        }
                    }
                }
            }
        },

        csslint: {
            minaintyg: {
                options: {
                    csslintrc: 'build/build-tools/csslint/.csslintrc',
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
                    jshintrc: 'build/build-tools/jshint/.jshintrc',
                    reporterOutput: '',
                    force: false,
                    ignores: ['**/*.min.js', '**/vendor/**']
                },
                src: [ 'Gruntfile.js', SRC_DIR + '**/*.js', TEST_DIR + '**/*.js', '!' + DEST_DIR + '/app.min.js',
                        '!' + DEST_DIR + '/base/app.min.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: 'karma.conf.ci.js',
                client: {
                    args: ['--skip-coverage=' + SKIP_COVERAGE]
                },
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR,
                    subdir: '.'
                }
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

        ngtemplates : grunt.util._.extend(buildObjectForAllModules(function(module) {
            return {
                cwd: __dirname + module.src,
                src: ['**/*.html'],
                dest: __dirname + module.dest + '/templates.js',
                options: {
                    module: module.angularModule,
                    url: function(url) {
                        return '/web/webjars/' + module.name + '/webcert/' + url;
                    }
                }
            };
        }),{
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
        }),

        connect: {
            server: {
                options: {
                    port: 8089,
                    base: 'src/main/webapp',
                    hostname: '*',
                    middleware: function(connect/*, options*/) {
                        var proxy = require('grunt-connect-proxy/lib/utils').proxyRequest;
                        var serveStatic = require('serve-static');
                        var middlewares = [];
                        middlewares.push(
                            connect().use(
                                '/web',
                                serveStatic(__dirname + '/src/main/webapp') // jshint ignore:line
                            ));
                        middlewares.push(
                            connect().use(
                                '/app',
                                serveStatic(__dirname + '/src/main/webapp/app') // jshint ignore:line
                            ));
                        middlewares.push(
                            connect().use(
                                '/app/app-deps.js',
                                serveStatic(__dirname + DEST_DIR + '/app-deps.js') // jshint ignore:line
                            ));
                        middlewares.push(
                            connect().use(
                                '/css',
                                serveStatic(__dirname + '/src/main/webapp/css') // jshint ignore:line
                            ));
                        Object.keys(modules).forEach(function(moduleName) {
                            var module = modules[moduleName];
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg',
                                    serveStatic(__dirname + module.src) //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg/templates.js',
                                    serveStatic(__dirname + module.dest + '/templates.js') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg/module-deps.json',
                                    serveStatic(__dirname + module.dest + '/js/module-deps.json') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg/css',
                                    serveStatic(__dirname + module.dest + '/css')//jshint ignore:line
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
        },

        watch: {
            js: {
                files: buildListForAllModules(function(module) {
                    return module.src.substring(1) + '/**/*.js';
                }).concat([SRC_DIR + '/**/*.js']),
                tasks: ['generateModuleDeps'],
                options: {
                    event: ['added', 'deleted']
                }
            },
            html: {
                files: buildListForAllModules(function(module) {
                    return __dirname + module.src + '/**/*.html';
                }).concat([ SRC_DIR + '/**/*.html' ]),
                tasks: ['ngtemplates']
            }
        }
    });

    grunt.registerTask('default', ['jshint', 'bower', 'wiredep', 'ngtemplates:minaintyg', 'concat', 'ngAnnotate', 'uglify' ]);
    grunt.registerTask('lint', [ 'jshint', 'csslint' ]);
    grunt.registerTask('test', [ 'karma' ]);
    grunt.registerTask('server', [ 'configureProxies:server', 'connect:server', 'generateModuleDeps', 'watch' ]);
};
