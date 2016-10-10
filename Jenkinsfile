#!groovy

def javaEnv() {
    def javaHome = tool 'JDK8u66'
    ["PATH=${env.PATH}:${javaHome}/bin", "JAVA_HOME=${javaHome}"]
}

stage('checkout') {
    node {
        checkout scm
    }
}

// stage('build') {
//     node {
//         withEnv(javaEnv()) {
//             sh './gradlew clean build uploadArchives tagRelease -DnexusUsername=$NEXUS_USERNAME -DnexusPassword=$NEXUS_PASSWORD -DgithubUser=$GITHUB_USERNAME -DgithubPassword=$GITHUB_PASSWORD --stacktrace'
//         }
//     }
// }

stage('deploy') {
    node {
        ansiblePlaybook extraVars: [version: "3.0.$BUILD_NUMBER", ansible_ssh_port: "22" ], \
            installation: 'ansible-yum', \
            inventory: 'ansible/hosts_test', \
            playbook: 'ansible/deploy.yml', \
            sudoUser: null
    }
}

stage('test') {
    node {
        withEnv(javaEnv()) {
            sh './gradlew restAssuredTest -DbaseUrl=http://intygstjanst.inera.nordicmedtest.se/'
        }
    }

    node {
        wrap([$class: 'Xvfb']) {
            withEnv(javaEnv()) {
                sh './gradlew fitnesseTest -Dgeb.env=firefoxRemote -Dweb.baseUrl=https://minaintyg.inera.nordicmedtest.se/web/ \
                    -Dcertificate.baseUrl=https://intygstjanst.inera.nordicmedtest.se/inera-certificate/ -PfileOutput'
            }
        }
    }
}
