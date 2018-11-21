#!groovy

def buildVersion = "3.6.0.${BUILD_NUMBER}"
def commonVersion = "3.6.0.+"
def infraVersion = "3.6.0.+"

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
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DinfraVersion=${infraVersion}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                 reportFiles: 'index.html', reportName: 'JUnit results'
        }
    }
}

stage('deploy') {
   node {
       util.run {
           ansiblePlaybook extraVars: [version: buildVersion, ansible_ssh_port: "22", deploy_from_repo: "false", config_version: "MI-3.5"], \
                installation: 'ansible-yum', inventory: 'ansible/inventory/minaintyg/test', playbook: 'ansible/deploy.yml'
           util.waitForServer('https://minaintyg.inera.nordicmedtest.se/version.jsp')
       }
   }
}

stage('restAssured') {
   node {
       try {
           shgradle "restAssuredTest -DbaseUrl=http://minaintyg.inera.nordicmedtest.se/ -Dcertificate.baseUrl=http://minaintyg.inera.nordicmedtest.se/inera-certificate/ \
                 -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DinfraVersion=${infraVersion}"
       } finally {
           publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'web/build/reports/tests/restAssuredTest', \
               reportFiles: 'index.html', reportName: 'RestAssured results'
       }
   }
}

stage('protractor') {
   node {
       try {
           sh(script: 'rm -rf test/node_modules/minaintyg-testtools') // Without this, node does not always recognize that a new version is available.
           wrap([$class: 'Xvfb']) {
               shgradle "protractorTests -Dprotractor.env=build-server \
                     -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DinfraVersion=${infraVersion}"
           }
       } finally {
           publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'test/dev/report', \
                reportFiles: 'index.html', reportName: 'Protractor results'
       }
   }
}

stage('tag and upload') {
    node {
        shgradle "uploadArchives tagRelease -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DinfraVersion=${infraVersion}"
    }
}

stage('notify') {
    node {
        util.notifySuccess()
    }
}
