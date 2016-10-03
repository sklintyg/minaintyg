#!groovy

def javaEnv() {
  def javaHome = tool 'JDK8u66'
  ["PATH=${env.PATH}:${javaHome}/bin", "JAVA_HOME=${javaHome}"]
}

stage 'checkout'

node {
  git url: 'https://github.com/sklintyg/minaintyg.git'
  checkout scm
}

stage 'build'

node {
  withEnv(javaEnv()) {
    sh './gradlew clean install'
  }
}

stage 'test'

node {
// withEnv(["PATH+MAVEN=${tool 'm3'}/bin"]) {
//     sh "mvn clean verify"
// }
}
