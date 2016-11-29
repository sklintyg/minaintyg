#!groovy

def buildVersion = "3.1.${BUILD_NUMBER}"
def commonVersion = "3.1.+"
def typerVersion = "3.1.+"

stage('checkout') {
    node {
        git url: "https://github.com/sklintyg/minaintyg.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }
}

stage('build') {
    node {
        try {
            shgradle "--refresh-dependencies clean build testReport sonarqube -PcodeQuality -PcodeCoverage -DgruntColors=false \
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                 reportFiles: 'index.html', reportName: 'JUnit results'
        }
    }
}

stage('deploy') {
    node {
        util.run {
            ansiblePlaybook extraVars: [version: buildVersion, ansible_ssh_port: "22", deploy_from_repo: "false"], \
                 installation: 'ansible-yum', inventory: 'ansible/hosts_test', playbook: 'ansible/deploy.yml'
            util.waitForServer('https://minaintyg.inera.nordicmedtest.se/version.jsp')
        }
    }
}

stage('restAssured') {
    node {
        try {
            shgradle "restAssuredTest -DbaseUrl=http://minaintyg.inera.nordicmedtest.se/ \
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'web/build/reports/tests/restAssuredTest', \
                reportFiles: 'index.html', reportName: 'RestAssured results'
        }
    }
}

stage('protractor') {
    node {
        try {
            wrap([$class: 'Xvfb']) {
                shgradle "protractorTests -Dprotractor.env=build-server \
                      -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
            }
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'test/dev/report', \
                 reportFiles: 'index.html', reportName: 'Protractor results'
        }
    }
}

stage('fitnesse') {
    node {
        try {
            wrap([$class: 'Xvfb']) {
                shgradle "fitnesseTest -Dgeb.env=firefoxRemote -Dweb.baseUrl=https://minaintyg.inera.nordicmedtest.se/web/ \
                      -Dcertificate.baseUrl=https://minaintyg.inera.nordicmedtest.se/inera-certificate/ -PfileOutput -PoutputFormat=html\
                      -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
            }
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'specifications/', \
                 reportFiles: 'fitnesse-results.html', reportName: 'Fitnesse results'
        }
    }
}

stage('tag and upload') {
    node {
        shgradle "uploadArchives tagRelease -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
    }
}

stage('notify') {
    node {
        util.notifySuccess()
    }
}
