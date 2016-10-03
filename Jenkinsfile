#!groovy

stage 'build'
node {
  git url: 'https://github.com/sklintyg/minaintyg.git'
  checkout scm
  sh 'gradlew clean install'
}

stage 'test'
// withEnv(["PATH+MAVEN=${tool 'm3'}/bin"]) {
//     sh "mvn clean verify"
// }
