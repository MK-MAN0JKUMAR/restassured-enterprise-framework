pipeline {

    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    parameters {

        choice(name: 'TEST_ENV',
                choices: ['qa','stage','prod'],
                description: 'Environment')

        choice(name: 'GROUPS',
                choices: ['smoke','regression','all'],
                description: 'Test group')

        string(name: 'SERVICES',
                defaultValue: 'all',
                description: 'Services (reqres,petstore,github or all)')
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
                    def serviceParam = params.SERVICES

                    def groupArg = ""
                    if(group != "all"){
                        groupArg = "-Dgroups=${group}"
                    }

                    def services = []

                    if(serviceParam == "all"){
                        services = ["reqres","petstore","github"]
                    } else {
                        services = serviceParam.split(",")
                    }

                    echo "Services to run: ${services}"

                    def branches = [:]

                    for(service in services){

                        def svc = service.trim()   // FIX closure bug

                        branches[svc] = {

                            dir("${env.WORKSPACE}@${svc}") {  // FIX workspace conflict

                                checkout scm

                                bat """
                            mvn clean test ^
                            -Denv=${envName} ^
                            ${groupArg} ^
                            -Dservice=${svc}
                            """

                            }

                        }
                    }

                    parallel branches

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
            echo "Tests completed successfully"
        }

        failure {
            echo "Tests failed"
        }

    }
}
