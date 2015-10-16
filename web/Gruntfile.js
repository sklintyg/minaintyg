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

    var minaintyg = grunt.file.readJSON(SRC_DIR + 'app-deps.json').map(function(file) {
        return file.replace(/\/app\//g, SRC_DIR);
    });

    var minaintygBase = [SRC_DIR + 'base/app.js'].concat(minaintyg);
    minaintyg = [SRC_DIR + 'app.js'].concat(minaintyg);

    grunt.initConfig({

        csslint: {
            minaintyg: {
                options: {
                    csslintrc: '../target/build-tools/csslint/.csslintrc',
                    force: true
                },
                src: [ SRC_DIR + '../**/*.css' ]
            }
        },

        concat: {
            minaintyg: {
                src: minaintyg,
                dest: SRC_DIR + 'app.min.js'
            },
            minaintygBase: {
                src: minaintygBase,
                dest: SRC_DIR + 'base/app.min.js'
            }
        },

        jshint: {
            minaintyg: {
                options: {
                    jshintrc: '../target/build-tools/jshint/.jshintrc',
                    force: true,
                    ignores: ['**/templates.js', '**/*.min.js', '**/vendor/*.js']
                },
                src: [ 'Gruntfile.js', SRC_DIR + '**/*.js', TEST_DIR + '**/*.js', '!' + SRC_DIR + '/app.min.js',
                        '!' + SRC_DIR + '/base/app.min.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: 'src/test/resources/karma.conf.ci.js',
                reporters: [ 'mocha' ]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: SRC_DIR + 'app.min.js',
                dest: SRC_DIR + 'app.min.js'
            },
            minaintygBase: {
                src: SRC_DIR + 'base/app.min.js',
                dest: SRC_DIR + 'base/app.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: SRC_DIR + 'app.min.js',
                dest: SRC_DIR + 'app.min.js'
            },
            minaintygBase: {
                src: SRC_DIR + 'base/app.min.js',
                dest: SRC_DIR + 'base/app.min.js'
            }
        },

        ngtemplates : {
            minaintyg: {
                cwd: __dirname + '/src/main/webapp',
                src: ['app/**/*.html'],
                dest: __dirname + '/src/main/webapp/app/templates.js',
                options: {
                    module: 'minaintyg',
                    url: function(url) {
                        return url.replace('../', '/');
                    }
                }
            }
        }
    });

    grunt.registerTask('default', ['ngtemplates:minaintyg', 'concat', 'ngAnnotate', 'uglify' ]);
    grunt.registerTask('lint', [ 'jshint', 'csslint' ]);
    grunt.registerTask('test', [ 'karma' ]);
};
