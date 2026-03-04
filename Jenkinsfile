pipeline {

    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    parameters {

        choice(
                name: 'TEST_ENV',
                choices: ['qa','stage','prod'],
                description: 'Environment to run tests'
        )

        choice(
                name: 'GROUPS',
                choices: ['smoke','regression','all'],
                description: 'Test group'
        )

        choice(
                name: 'SERVICES',
                choices: ['reqres','petstore','github','all'],
                description: 'Service to test'
        )
    }

    environment {
        MAVEN_OPTS = "-Xmx1024m"
        GITHUB_TOKEN = credentials('github-token')
        GITHUB_USERNAME = credentials('github-username')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/MK-MAN0JKUMAR/restassured-enterprise-framework.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -version'
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Execute API Tests') {
            steps {

                script {

                    def envName = params.TEST_ENV
                    def group = params.GROUPS
                    def service = params.SERVICES

                    def groupFilter = ""

                    if(group == "all" && service == "all") {

                        bat "mvn clean test -Denv=${envName}"

                    }

                    else if(group == "all") {

                        groupFilter = service

                        bat "mvn clean test -Denv=${envName} -Dgroups=${groupFilter}"

                    }

                    else if(service == "all") {

                        groupFilter = group

                        bat "mvn clean test -Denv=${envName} -Dgroups=${groupFilter}"

                    }

                    else {

                        groupFilter = "${group},${service}"

                        bat "mvn clean test -Denv=${envName} -Dgroups=${groupFilter}"

                    }

                }

            }
        }

        stage('Generate Allure Report') {
            steps {
                bat 'mvn allure:report || exit 0'
            }
        }
    }

    post {

        always {

            archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**/*', allowEmptyArchive: true

            echo "Build finished."
        }

        success {
            echo 'Tests completed successfully'
        }

        failure {
            echo 'Tests failed'
        }
    }
}