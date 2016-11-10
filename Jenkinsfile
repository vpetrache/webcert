#!groovy

def buildVersion = "5.0.${BUILD_NUMBER}"
def commonVersion = "3.0.+"
def typerVersion = "3.0.+"

stage('checkout') {
    node {
        util.run { checkout scm }
    }
}

stage('build') {
    node {
        shgradle "--refresh-dependencies clean camelTest build sonarqube -PcodeQuality -DgruntColors=false \
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
    }
}

stage('deploy') {
    node {
        util.run {
            ansiblePlaybook extraVars: [version: buildVersion, ansible_ssh_port: "22", deploy_from_repo: "false", config_version: "webcert-5.0.0-RC5"],  \
                 installation: 'ansible-yum',  \
                 inventory: 'ansible/hosts_test',  \
                 playbook: 'ansible/deploy.yml',  \
                 sudoUser: null
        }
    }
}

stage('restAssured') {
    node {
        shgradle "restAssuredTest -DbaseUrl=http://webcert.inera.nordicmedtest.se/ \
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
    }
}

stage('protractor') {
    node {
        wrap([$class: 'Xvfb']) {
            shgradle "protractorTests -Dprotractor.env=build-server \
                      -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
        }
    }
}

stage('fitnesse') {
    node {
        wrap([$class: 'Xvfb']) {
            shgradle "fitnesseTest -PfileOutput -Dgeb.env=firefoxRemote -Dweb.baseUrl=https://webcert.inera.nordicmedtest.se/ \
                      -DbaseUrl=https://webcert.inera.nordicmedtest.se/ -Dlogsender.baseUrl=https://webcert.inera.nordicmedtest.se/log-sender/ \
                      -Dcertificate.baseUrl=https://intygstjanst.inera.nordicmedtest.se/inera-certificate/ \
                      -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
        }
    }
}

stage('tag and upload') {
    node {
        shgradle "uploadArchives tagRelease -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion} -DtyperVersion=${typerVersion}"
    }
}
