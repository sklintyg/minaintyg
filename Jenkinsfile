@Library('essLib')_

Map cloneInfo
String gitUrl
String gitBranch
String buildTag
String project
String artifact
String version
String deployName
String deployArtifact
String commit

String gradleBuildArgs
String infraVersion
String commonVersion
String resolvedInfraVersion
String resolvedCommonVersion
String dockerRegistry
String dockerCredential
String dockerfile
String builderImage
String builderImageTag
String builderImageTagPostfix
String runtimeImage
String runtimeImageTag
String setLatestTag
String triggerDeployBks
String skipDeploySit2
String contextPath

String buildUrl
String buildName
String currentStage
String recipients
String pwd
String whichDocker
List<Map<String, String>> allCommitIds = []
Map<String, String> error = [stage: '', error: '']

pipeline {
    agent any

    stages {
        stage('Setup') {
            environment {
                GIT_AUTH = credentials('intyg-github')
            }
            steps {
                script {
                    try {
                        currentStage = STAGE_NAME
                        buildUrl = BUILD_URL
                        buildName = "${currentBuild.projectName}/${currentBuild.number}"
                        recipients = essJob.getProperty(name: 'notify.error')

                        gitUrl = GIT_REPO_URL
                        gitBranch = GIT_REPO_BRANCH
                        env.ENV_GIT_URL = gitUrl
                        env.ENV_GIT_BRANCH = gitBranch
                        cloneInfo = essGit.clone url: gitUrl, branch: gitBranch
                        allCommitIds = getAllCommitIdsSinceLastSuccess(gitUrl, gitBranch)

                        project = essJob.getProperty(name: 'project.name')
                        artifact = essJob.getProperty(name: 'artifact.name')
                        version = bumpVersion(essCmn.getVersion() as String)
                        deployName = essJob.getProperty(name: 'deploy.name')
                        deployArtifact = deployName ? deployName : artifact
                        buildTag = "v${version}" as String
                        essJob.tagBuildName tag: buildTag

                        gradleBuildArgs = essJob.getProperty(name: 'build.args')
                        infraVersion = essJob.getProperty(name: 'dependencies.infra.version')
                        commonVersion = essJob.getProperty(name: 'dependencies.common.version')
                        dockerRegistry = essJob.getProperty(name: 'docker.repo')
                        dockerCredential = essJob.getProperty(name: 'docker.credential')
                        builderImage = essJob.getProperty(name: 'builder.image')
                        builderImageTag = essJob.getProperty(name: 'builder.image.tag')
                        builderImageTagPostfix = params.USE_CACHED_BUILDER ? "-${artifact}" : ''
                        runtimeImage = essJob.getProperty(name: 'runtime.image')
                        runtimeImageTag = essJob.getProperty(name: 'runtime.image.tag')

                        String contextPathConfig = essJob.getProperty(name: 'context.path')
                        contextPath = contextPathConfig ? contextPathConfig : 'ROOT'

                        //setLatestTag = SET_LATEST_TAG
                        //triggerDeployBks = TRIGGER_DEPLOY_BKS
                        //skipDeploySit2 = SKIP_DEPLOY_SIT2
                        String s = sh (script: "pwd", returnStdout: true).toString().trim()
                        pwd = "${s}/integration-test/src/test/java"

                        whichDocker = sh (script: "which docker", returnStdout: true).toString().trim()
                        println("whichDocker: ${whichDocker}")

                        String group = sh (script: "cat /etc/group | grep docker | grep -oPm1 (?<=docker:x:)(\\d*)", returnStdout: true).toString().trim()
                        println("group1: ${group}")


                    } catch(e) {
                        error = [stage: env.STAGE_NAME, error: e as String]
                        throw e
                    }
                }
            }
        }

        stage('Build') {
            agent {
                docker {
                    image "${dockerRegistry}/${project}/${builderImage}:${builderImageTag}${builderImageTagPostfix}"
                    registryUrl "https://${dockerRegistry}"
                    registryCredentialsId dockerCredential
                    reuseNode true
                    alwaysPull true
                    args "-v ${pwd}:${pwd} -w ${pwd} -v /var/run/docker.sock:/var/run/docker.sock -v ${whichDocker}:${whichDocker}"
                }
            }
            steps {
                script {
                    try {
                        script {

                            String id = sh (script: "id", returnStdout: true).toString().trim()
                            println("id: ${id}")

                            String sock = sh (script: "ls -l /var/run/docker.sock", returnStdout: true).toString().trim()
                            println("sock: ${sock}")

                            String group = sh (script: "cat /etc/group", returnStdout: true).toString().trim()
                            println("group: ${group}")

                            currentStage = STAGE_NAME
                            sh script: "gradle ${gradleBuildArgs} -DbuildVersion=${version} -DinfraVersion=${infraVersion} \
                                -DcommonVersion=${commonVersion} -Dfile.encoding=UTF-8"

                            resolvedInfraVersion = resolveLibraryVersion(artifact, 'infra', infraVersion, 'dependencies.infra.version.resolved')
                            resolvedCommonVersion = resolveLibraryVersion(artifact, 'common', commonVersion, 'dependencies.common.version.resolved')

                        }
                    } catch(e) {
                        error = [stage: STAGE_NAME, error: e as String]
                        throw e
                    }
                }
            }
        }

        /*
        stage('Commit') {
            steps {
                script {
                    try {
                        currentStage = STAGE_NAME
                        sh script: "git commit Jenkins.properties -m \"(Jenkins2 pipeline) [ci skip] Release of ${buildTag}\"",
                                returnStatus: true
                        commit = essGit.getCommit()
                    } catch(e) {
                        error = [stage: STAGE_NAME, error: e as String]
                        throw e
                    }
                }
            }
        }

        stage('Build Image') {
            steps {
                script {
                    try {
                        currentStage = STAGE_NAME
                        final String fromImage = "${dockerRegistry}/${project}/${runtimeImage}:${runtimeImageTag}" as String
                        final String artifactName = getArtifactName(project, artifact)
                        final String buildOpts = "--pull"
                        final String buildArgs = getBuildArgs(fromImage, project, artifact, version, resolvedInfraVersion,
                                resolvedCommonVersion, contextPath, gitUrl, commit)
                        final String dockerfilePath = '.'
                        final String dockerfileName = getFilename(dockerfile, dockerfilePath)
                        final String buildParams = "${buildOpts} ${buildArgs} ${dockerfileName} ${dockerfilePath}" as String

                        docker.withRegistry("https://${dockerRegistry}", dockerCredential) {
                            def builtImage = docker.build(artifactName, buildParams)
                            builtImage.push(version)

                            if (setLatestTag.toBoolean()) {
                                builtImage.push('latest')
                            }
                        }
                    } catch(e) {
                        error = [stage: STAGE_NAME, error: e as String]
                        throw e
                    }
                }
            }
        }

        stage('Tag and Push') {
            environment {
                GIT_AUTH = credentials('intyg-github')
            }
            steps {
                script {
                    try {
                        currentStage = STAGE_NAME
                        echo "Tag and push (Workaround until essGit can handle this for GitHub-repos)"
                        sh("""\
                            git config --local credential.helper "!f() { echo username=\\\$GIT_AUTH_USR; echo password=\\\$GIT_AUTH_PSW; }; f"
                            git tag -a ${buildTag} -m "Release of ${buildTag}"
                            git push --set-upstream origin ${gitBranch}
                            git push --follow-tags
                        """.stripIndent())
                    } catch(e) {
                        error = [stage: STAGE_NAME, error: e as String]
                        throw e
                    }
                }
            }
        }

        stage('Trigger Deploy DEVTEST') {
            when {
                expression { triggerDeployBks.toBoolean() }
            }
            steps {
                script {
                    currentStage = env.STAGE_NAME
                    build job: 'deploy/deploy-bks', wait: false, propagate: false,
                            parameters: [
                                    string(name: 'APPLICATION', value: deployArtifact),
                                    string(name: 'ENVIRONMENT', value: 'devtest'),
                                    string(name: 'VERSION', value: version)
                            ]
                }
            }
        }

        stage('Trigger Deploy SIT2') {
            when {
                expression { triggerDeployBks.toBoolean() && !skipDeploySit2.toBoolean() }
            }
            steps {
                script {
                    currentStage = env.STAGE_NAME
                    build job: 'deploy/deploy-bks', wait: false, propagate: false,
                            parameters: [
                                    string(name: 'APPLICATION', value: deployArtifact),
                                    string(name: 'ENVIRONMENT', value: 'sit2'),
                                    string(name: 'VERSION', value: version)
                            ]
                }
            }
        }*/
    }

    /*post {
        success {
            script {
                String message = getSuccessMessage(buildUrl, buildName, gitUrl, gitBranch, artifact, version, resolvedInfraVersion,
                        resolvedCommonVersion, allCommitIds)
                println("SUCCESS_MESSAGE:\n${message}")
                essNotify.chat(recipients: recipients, message: message, color: 'green')
            }
        }

        failure {
            script {
                List<String> logErrors = getLogErrors()
                error.error = updateErrorMessage(error.error, error.stage, logErrors, gitUrl)
                String message = getFailMessage(buildUrl, buildName, gitUrl, gitBranch, artifact, allCommitIds, currentStage, error)
                String color = error.error.contains("Concurrent commit.") ? 'yellow' : 'red'
                println("FAILURE_MESSAGE:\n${message}")
                essNotify.chat(recipients: recipients, message: message, color: color)
            }
        }
    }*/
}

private String bumpVersion(String previousVersion) {
    final String bumpedVersion =  essCmn.bumpVersion(version: previousVersion)
    essCmn.setVersion(version: bumpedVersion)
    return bumpedVersion
}

private static String getFilename(String dockerfile, String dockerfilePath) {
    return dockerfile ? "--file ${dockerfilePath}/${dockerfile}" as String : ''
}

private static String getArtifactName(String project, String artifact) {
    return "${project.toLowerCase()}/${artifact}" as String
}

private String resolveLibraryVersion(String artifact, String libName, String libVersion, String propertyName) {
    String resolvedLibVersion = 'N/A'
    String dir = artifact != 'logsender' ? "${artifact}-web" : ''
    if (libVersion) {
        resolvedLibVersion = sh (script: "gradle ${dir}:dependencyInsight --dependency 'se.inera.intyg.${libName}:' \
            --configuration compileClasspath -D${libName}Version=${libVersion} | grep -oPm1 '(?<=:)\\d+(.\\d+){2,}'", returnStdout: true)
                .toString().trim()
        essJob.updateProperty(name: propertyName, value: resolvedLibVersion)
    }
    return resolvedLibVersion
}

private static String getBuildArgs(String fromImage, String project, String artifact, String artifactVersion, String resolvedInfraVersion,
                                   String resolvedCommonVersion, String contextPath, String gitUrl, String commit) {
    return "--build-arg from_image=${fromImage} \
            --build-arg project_name=${project} \
            --build-arg artifact=${artifact} \
            --build-arg artifact_name=${getArtifactName(project, artifact)} \
            --build-arg artifact_version=${artifactVersion} \
            --build-arg infra_version=${resolvedInfraVersion} \
            --build-arg common_version=${resolvedCommonVersion} \
            --build-arg context_path=${contextPath} \
            --build-arg vcs_url=${gitUrl} \
            --build-arg vcs_ref=${commit}" as String
}

private List<Map<String, String>>  getAllCommitIdsSinceLastSuccess(String gitUrl, String gitBranch) {
    List<Map<String, String>> commitIds = []
    String buildGitUrl = gitUrl
    String buildGitBranch = gitBranch
    def build = currentBuild

    while(build && buildGitBranch && !(buildGitUrl == gitUrl && buildGitBranch == gitBranch && build.result == 'SUCCESS')) {
        for (def changeLog in build.changeSets) {
            for(def entry in changeLog.items) {
                if (buildGitUrl == gitUrl && buildGitBranch == gitBranch) {
                    commitIds.add([ commitId: entry.getCommitId() as String, timestamp: entry.getTimestamp() as String ])
                }
            }
        }
        build = build.previousBuild
        if (build) {
            buildGitUrl = build.buildVariables["ENV_GIT_URL"]
            buildGitBranch = build.buildVariables["ENV_GIT_BRANCH"]
            print("Analyzing previous build: ${build.getNumber()}, repo: ${buildGitUrl}, branch: ${buildGitBranch} for commits")
        }
    }
    return sortCommitIdsByTimestamp(commitIds)
}

@NonCPS
private List<Map<String, String>> sortCommitIdsByTimestamp(List<Map<String, String>> commitIds) {
    commitIds.sort {a, b -> b.timestamp <=> a.timestamp }
    println("COMMITS_SINCE_PREVIUOS_SUCCESS:\n${commitIds.join("\n")}")
    return commitIds
}

private List<String> getLogErrors() {
    List<String> errors = []
    def logErrors = essJob.searchLog(regex: /(?m)(?i)^(?>\[\d{4}(?>-\d{2})+T\d{2}(?>:\d{2})+\.\d+Z\]\s+)?(?>error:\s|failure:\s)(\S.+)$/)

    if (!logErrors) {
        return errors
    }
    if (logErrors.get(0) instanceof String) {
        errors.add(logErrors.get(1) as String)
    } else {
        logErrors.each { errors.add(it.get(1) as String) }
    }

    println("LOG_ERRORS: ${errors}")
    return errors
}

private String updateErrorMessage(String error, String errorStage, List<String> logErrors, String gitUrl) {
    boolean concurrentCommit = errorStage == 'Tag and Push' && logErrors.any { it =~ /failed to push some refs to '${gitUrl}'/ }
    if (concurrentCommit) {
        int nextBuild = currentBuild.getNextBuild()?.getNumber()
        if (nextBuild) {
            return "Concurrent commit. New build with number ${nextBuild} has been started."
        } else {
            return "Concurrent commit. New build will start shortly."
        }
    }
    if ((!error || error.contains("exit code 1")) && logErrors) {
        return logErrors.last()
    }
    return error
}

private String getSuccessMessage(String buildUrl, String buildName, String gitUrl, String gitBranch, String artifact, String version,
                                 String infraVersion, String commonVersion, List<Map<String, String>> allCommits) {
    List<String> commits = getCommits(allCommits)
    String tagLink = getTagLink(gitUrl, artifact, artifact, version)
    String commonTagLink = getTagLink(gitUrl, artifact, 'common', commonVersion)
    String infraTagLink = getTagLink(gitUrl, artifact, 'infra', infraVersion)
    return """\
        SUCCESS! Successfully built *\\<${gitUrl}|${artifact}\\>* from branch *${gitBranch}*
        Artifact: *${artifact}:${tagLink}* [commonVersion: ${commonTagLink}, infraVersion: ${infraTagLink}]
        Build job: \\<${buildUrl}|${buildName}\\>
        Number of commits: ${commits.size()}
    """.stripIndent().concat(commits.join("\n\n"))
}

private String getFailMessage(String buildUrl, String buildName, String gitUrl, String gitBranch, String artifact,
                              List<Map<String, String>> allCommits, String currentStage, Map<String, String> error) {
    List<String> commits= getCommits(allCommits)
    String failingStage = !error?.stage ? "last successful stage was ${currentStage}" : error.stage
    return """\
        FAILURE! Build of *\\<${gitUrl}|${artifact}\\>* from branch *${gitBranch}* failed
        Build job: \\<${buildUrl}|${buildName}\\>
        Failing stage: ${failingStage}
        Cause of fail: ${!error.error ? 'Cause of failure could not be determined' : error.error.trim()}        
        Number of commits: ${commits.size()}
    """.stripIndent().concat(commits.join("\n\n"))
}

private List<String> getCommits(List<Map<String, String>> allCommitIds) {
    List<String> commits = []
    for (Map<String, String> id : allCommitIds) {
        String commit = sh (script: "git show --format='%h | %an | %s' -s ${id.commitId} \
            || echo ''", returnStdout: true).trim()
        if (commit) {
            commits.add("\\>${commit}")
        }
    }
    commits.removeAll{it.matches(/.*\[ci skip].*/)}
    return commits
}

private static String getTagLink(String gitUrl, String buildArtifact, String tagArtifact, String version) {
    if (version == 'N/A') {
        return version
    }
    return """\\<${gitUrl.replaceFirst(/${buildArtifact}.git/, "${tagArtifact}/releases/tag/v${version}")}|${version}\\>"""
}
