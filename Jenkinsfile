#!groovy

stage 'build'
node {
  git url: 'https://github.com/sklintyg/minaintyg.git'
  checkout scm
  env.JAVA_HOME="${tool 'JDK8u66'}"
  env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
  sh './gradlew clean install'
}

stage 'test'
// withEnv(["PATH+MAVEN=${tool 'm3'}/bin"]) {
//     sh "mvn clean verify"
// }
