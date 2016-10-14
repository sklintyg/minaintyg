#!groovy

def buildVersion  = "3.0.${BUILD_NUMBER}"
def commonVersion = "3.0.+"
def typerVersion  = "3.0.+"

stage('checkout') {
    node {
        util.run { checkout scm }
    }
}

stage('build') {
    node {
        shgradle "--refresh-dependencies clean build sonarqube -PcodeQuality -DgruntColors=false \
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
    }
}

stage('deploy') {
    node {
        util.run {
            ansiblePlaybook extraVars: [version: buildVersion, ansible_ssh_port: "22", deploy_from_repo: "false"], \
                installation: 'ansible-yum', \
                inventory: 'ansible/hosts_test', \
                playbook: 'ansible/deploy.yml', \
                sudoUser: null
        }
    }
}

stage('integration tests') {
    node {
        wrap([$class: 'Xvfb']) {
            shgradle "fitnesseTest -Dgeb.env=firefoxRemote -Dweb.baseUrl=https://minaintyg.inera.nordicmedtest.se/web/ \
                      -Dcertificate.baseUrl=https://intygstjanst.inera.nordicmedtest.se/inera-certificate/ -PfileOutput \
                      -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"

            shgradle "protractorTests -Dprotractor.env=build-server \
                      -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
        }
    }
}

stage('tag and upload') {
    node {
        shgradle "uploadArchives tagRelease -DnexusUsername=$NEXUS_USERNAME -DnexusPassword=$NEXUS_PASSWORD -DgithubUser=$GITHUB_USERNAME \
                  -DgithubPassword=$GITHUB_PASSWORD -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
    }
}
