#!groovy

def javaEnv() {
    def javaHome = tool 'JDK8u66'
    ["PATH=${env.PATH}:${javaHome}/bin", "JAVA_HOME=${javaHome}"]
}

stage('checkout') {
    try {
        node {
            checkout scm
        }
    } catch (e) {
        currentBuild.result = "FAILED"
        notifyFailed()
        throw e
    }
}

stage('build') {
    try {
        node {
            withEnv(javaEnv()) {
                sh './gradlew --refresh-dependencies clea build sonarqube -PcodeQuality'
            }
        }
    } catch (e) {
        currentBuild.result = "FAILED"
        notifyFailed()
        throw e
    }
}

stage('deploy') {
    try {
        node {
            ansiblePlaybook extraVars: [version: "3.0.$BUILD_NUMBER", ansible_ssh_port: "22", deploy_from_repo: "false"], \
                installation: 'ansible-yum', \
                inventory: 'ansible/hosts_test', \
                playbook: 'ansible/deploy.yml', \
                sudoUser: null
        }
    } catch (e) {
        currentBuild.result = "FAILED"
        notifyFailed()
        throw e
    }
}

stage('integration tests') {
    try {
        node {
            wrap([$class: 'Xvfb']) {
                withEnv(javaEnv()) {
                    sh './gradlew fitnesseTest -Dgeb.env=firefoxRemote -Dweb.baseUrl=https://minaintyg.inera.nordicmedtest.se/web/ \
                    -Dcertificate.baseUrl=https://intygstjanst.inera.nordicmedtest.se/inera-certificate/ -PfileOutput'
                }
            }
        }

        node {
            wrap([$class: 'Xvfb']) {
                withEnv(javaEnv()) {
                    sh './gradlew protractorTests -Dprotractor.env=build-server'
                }
            }
        }
    } catch (e) {
        currentBuild.result = "FAILED"
        notifyFailed()
        throw e
    }
}

stage('tag and upload') {
    try {
        node {
            withEnv(javaEnv()) {
                sh './gradlew uploadArchives tagRelease -DnexusUsername=$NEXUS_USERNAME -DnexusPassword=$NEXUS_PASSWORD \
                -DgithubUser=$GITHUB_USERNAME -DgithubPassword=$GITHUB_PASSWORD'
            }
        }
    } catch (e) {
        currentBuild.result = "FAILED"
        notifyFailed()
        throw e
    }
}

def notifyFailed() {
    emailext (
      subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
              body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
              recipientProviders: [[$class: 'CulpritsRecipientProvider'],[$class: 'DevelopersRecipientProvider']]
        )
}
