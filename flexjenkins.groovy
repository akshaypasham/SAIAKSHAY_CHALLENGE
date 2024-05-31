pipeline {
    agent any

    triggers {
        githubPush()
    }

    parameters {
        booleanParam(
            name: 'SKIP_FILE_CHECK',
            defaultValue: false,
            description: 'Set to true to skip the file change check and proceed with the build.'
        )
    }

    environment {
        TARGET_FILE = 'code2.py'
        TARGET_BRANCH = 'hello'
    }

    stages {
        stage('Pre-check File Change') {
            when {
                expression { return !params.SKIP_FILE_CHECK }
            }
            steps {
                script {
                    // Checkout the specific branch to get the latest changes
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${TARGET_BRANCH}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [],
                        userRemoteConfigs: [[url: 'https://github.com/akshaypasham/SAIAKSHAY_CHALLENGE.git']]
                    ])
                    
                    // Get the list of changed files in the latest commit
                    def changedFiles = sh(script: "git diff-tree --no-commit-id --name-only -r HEAD", returnStdout: true).trim().split('\n')

                    // Check if the specific file is in the list of changed files
                    if (!changedFiles.contains(TARGET_FILE)) {
                        echo "${TARGET_FILE} has not changed. Exiting the pipeline..."
                        // Exit the pipeline early if the specific file has not changed
                        currentBuild.result = 'NOT_BUILT'
                        error("Pipeline aborted because the specific file has not changed.")
                    } else {
                        echo "${TARGET_FILE} has changed. Proceeding with the build..."
                    }
                }
            }
        }

        stage('Checkout') {
            when {
                expression { currentBuild.result != 'NOT_BUILT' || params.SKIP_FILE_CHECK }
            }
            steps {
                // Checkout the specific branch
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${TARGET_BRANCH}"]],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [],
                        userRemoteConfigs: [[url: 'https://github.com/akshaypasham/SAIAKSHAY_CHALLENGE.git']]
                    ])
                }
            }
        }

        stage('Build') {
            when {
                expression { currentBuild.result != 'NOT_BUILT' || params.SKIP_FILE_CHECK }
            }
            steps {
                echo 'Running the build...'
                // Add your build steps here
            }
        }
    }
}
