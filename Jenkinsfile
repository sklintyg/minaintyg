#!groovy

env.JAVA_HOME="${tool 'JDK8u66'}"
env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

stage 'checkout'

node {
  git url: 'https://github.com/sklintyg/minaintyg.git'
  checkout scm
}

stage 'build'

node {
  sh './gradlew clean install'
}

stage 'test'

node {
// withEnv(["PATH+MAVEN=${tool 'm3'}/bin"]) {
//     sh "mvn clean verify"
// }
}
