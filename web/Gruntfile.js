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
        ngtemplates: 'grunt-angular-templates',
        postcss: 'grunt-postcss',
        sasslint: 'grunt-sass-lint'
    });

    var sass = require('node-sass');

    var WEB_DIR = 'src/main/webapp';
    var SRC_DIR = WEB_DIR + '/app/';
    var CSS_MICOMMON_DEST_DIR = '/../../common/web/build/resources/main/META-INF/resources/webjars/common/minaintyg/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = (grunt.option('outputDir') || 'build/webapp/') +  'app/';
    var TEST_OUTPUT_DIR = (grunt.option('outputDir') || 'build/karma/');
    var RUN_COVERAGE = grunt.option('run-coverage') !== undefined ? grunt.option('run-coverage') : false;

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

    var fileToInjectCss = grunt.file.expand([WEB_DIR + '/WEB-INF/pages/*.jsp', WEB_DIR + '/*.jsp']);
    var _ = require('lodash');

    var modules = {
        'common':      { base: 'common/web' },
        'fk7263':      { base: 'common/fk7263' },
        'ts-bas':      { base: 'common/ts/ts-bas' },
        'ts-diabetes': { base: 'common/ts/ts-diabetes' },
        'luse':        { base: 'common/fk/luse', angularModule:'luse' },
        'lisjp':       { base: 'common/fk/lisjp', angularModule:'lisjp' },
        'luae_na':     { base: 'common/fk/luae_na', angularModule:'luae_na' },
        'luae_fs':     { base: 'common/fk/luae_fs', angularModule:'luae_fs' }
    };
    Object.keys(modules).forEach(function(moduleName) {
        var module = modules[moduleName];
        module.name = moduleName;
        if (!module.angularModule) {
            module.angularModule = moduleName;
        }
        module.src = '/../../' + module.base + '/src/main/resources/META-INF/resources/webjars/' + moduleName;
        module.dest = '/../../' + module.base + '/build/resources/main/META-INF/resources/webjars/' + moduleName;
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
                ['app-shared/**/*.js', 'minaintyg/**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/module.js']).sort();
            grunt.file.write(__dirname + module.dest + '/minaintyg/module-deps.json', JSON.stringify(files.
            map(function(file) {
                return '/web/webjars/' + module.name + '/' + file;
            }).
            concat('/web/webjars/' + module.name + '/minaintyg/templates.js'), null, 4));
        });
    });

    grunt.initConfig({

        config: {
            // configurable paths
            client: WEB_DIR
        },

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
                    jshintrc: 'build/build-tools/jshint/jshintrc',
                    reporterOutput: '',
                    force: false,
                    ignores: ['**/*.min.js', '**/vendor/**']
                },
                src: [ 'Gruntfile.js', SRC_DIR + '**/*.js', TEST_DIR + '**/*.js', '!' + DEST_DIR + '/app.min.js',
                        '!' + DEST_DIR + '/base/app.min.js' ]
            }
        },

        sasslint: {
            options: {
                //configFile: 'config/.sass-lint.yml' //For now we use the .sass-lint.yml that is packaged with sass-lint
            },
            target: [SRC_DIR + '**/*.scss']
        },

        karma: {
            minaintyg: {
                configFile: 'karma.conf.ci.js',
                client: {
                    args: ['--run-coverage=' + RUN_COVERAGE]
                },
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR,
                    subdir: '.'
                }
            }
        },

        // Compiles Sass to CSS
        sass: {
            options: {
                implementation: sass,
                sourceMap: false
            },
            dist: {
                files: {
                    '<%= config.client %>/app/app.css': '<%= config.client %>/app/app.scss'
                }
            }
        },

        postcss: {
            options: {
                map: false,
                processors: [
                    require('autoprefixer')({browsers: ['last 2 versions', 'ie 9']}), // add vendor prefixes
                    require('cssnano')({zindex: false}) // minify the result
                ]
            },
            dist: {
                src: '<%= config.client %>/app/*.css'
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
                src: ['{minaintyg,app-shared}/**/*.html'],
                dest: __dirname + module.dest + '/minaintyg/templates.js',
                options: {
                    module: module.angularModule,
                    url: function(url) {
                        return '/web/webjars/' + module.name + '/' + url;
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
                        middlewares.push(require('connect-livereload')());
                        middlewares.push(
                            connect().use(
                                '/welcome.html',
                                serveStatic(__dirname + '/src/main/webapp/welcome.html') // jshint ignore:line
                            ));
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
                                '/img',
                                serveStatic(__dirname + '/src/main/webapp/img') // jshint ignore:line
                            ));
                        middlewares.push(
                            connect().use(
                                '/app/app-deps.js',
                                serveStatic(__dirname + DEST_DIR + '/app-deps.js') // jshint ignore:line
                            ));

                        Object.keys(modules).forEach(function(moduleName) {
                            var module = modules[moduleName];
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg',
                                    serveStatic(__dirname + module.src + '/minaintyg') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/app-shared',
                                    serveStatic(__dirname + module.src + '/app-shared') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg/templates.js',
                                    serveStatic(__dirname + module.dest + '/minaintyg/templates.js') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg/module-deps.json',
                                    serveStatic(__dirname + module.dest + '/minaintyg/module-deps.json') //jshint ignore:line
                                ));
                            middlewares.push(
                                connect().use(
                                    '/web/webjars/'+module.name+'/minaintyg/css',
                                    serveStatic(__dirname + module.dest + '/minaintyg/css')//jshint ignore:line
                                ));
                        });

                        //load mi-common from common build dir
                        middlewares.push(
                            connect().use(
                                '/web/webjars/common/minaintyg/mi-common.css',
                                serveStatic(__dirname + CSS_MICOMMON_DEST_DIR + 'mi-common.css') //jshint ignore:line
                            ));

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

        injector: {
            options: {
                lineEnding: grunt.util.linefeed
            },

            // Inject component scss into app.scss
            sass: {
                options: {
                    transform: function(filePath) {
                        filePath = filePath.replace('/src/main/webapp/app/', '');
                        return '@import \'' + filePath + '\';';
                    },
                    starttag: '// injector',
                    endtag: '// endinjector'
                },
                files: {
                    '<%= config.client %>/app/app.scss': [
                        '<%= config.client %>/app/!(mixins)/**/*.{scss,sass}',
                        '!<%= config.client %>/app/app.{scss,sass}'
                    ]
                }
            },

            // Inject component css into index.html
            css: {
                options: {
                    transform: function(filePath) {
                        filePath = filePath.replace('/src/main/webapp/', '');
                        filePath = filePath.replace('/<%= config.tmp %>/', '');
                        return '<link rel="stylesheet" href="/' + filePath + '?_v=<spring:message code="buildNumber" />">';
                    },
                    starttag: '<!-- injector:css -->',
                    endtag: '<!-- endinjector -->'
                },
                files: _(fileToInjectCss).map(function(dest) {
                    return [dest, '<%= config.client %>/{app,font}/**/*.css'];
                }).fromPairs().value()
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
            },
            injectSass: {
                files: [
                    '<%= config.client %>/app/**/*.{scss,sass}'],
                tasks: ['injector:sass']
            },
            sass: {
                files: [
                    '<%= config.client %>/app/**/*.{scss,sass}'],
                tasks: ['sass', 'postcss']
            },
            livereload: {
                files: [
                    '<%= config.client %>/*.html',
                    '<%= config.client %>/app/**/*.scss',
                    '<%= config.client %>/app/**/*.html',
                    '<%= config.client %>/app/**/*.js',
                    '!<%= config.client %>/app/**/*.spec.js',
                    '!<%= config.client %>/app/**/*.mock.js',
                    '<%= config.client %>/img/{,*//*}*.{png,jpg,jpeg,gif,webp,svg}'
                ],
                options: {
                    livereload: true
                }
            }
        }
    });

    grunt.registerTask('default', [
        'bower',
        'injector:sass',
        'sass',
        'postcss',
        'injector:css',
        'wiredep',
        'ngtemplates:minaintyg',
        'concat',
        'ngAnnotate',
        'uglify' ]);
    grunt.registerTask('lint', [ 'jshint' ]);
    grunt.registerTask('test', [ 'bower', 'karma' ]);
    grunt.registerTask('server', [ 'configureProxies:server', 'connect:server', 'generateModuleDeps', 'watch' ]);
};
