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

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/MK-MANOJKUMAR/restassured-enterprise-framework.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -DskipTests'
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

                        sh """
                        mvn clean test \
                        -Denv=${envName}
                        """

                    }
                    else if(group == "all") {

                        groupFilter = service

                        sh """
                        mvn clean test \
                        -Denv=${envName} \
                        -Dgroups=${groupFilter}
                        """

                    }
                    else if(service == "all") {

                        groupFilter = group

                        sh """
                        mvn clean test \
                        -Denv=${envName} \
                        -Dgroups=${groupFilter}
                        """

                    }
                    else {

                        groupFilter = "${group},${service}"

                        sh """
                        mvn clean test \
                        -Denv=${envName} \
                        -Dgroups=${groupFilter}
                        """

                    }

                }

            }
        }

        stage('Generate Allure Report') {
            steps {
                sh 'mvn allure:report'
            }
        }
    }

    post {

        always {

            archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true

            archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**/*', allowEmptyArchive: true

        }

        success {
            echo 'Tests completed successfully'
        }

        failure {
            echo 'Tests failed'
        }
    }
}