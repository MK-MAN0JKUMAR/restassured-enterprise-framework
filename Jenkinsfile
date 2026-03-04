stage('Execute API Tests') {

    steps {

        script {

            def envName = params.TEST_ENV
            def group = params.GROUPS
            def service = params.SERVICES

            echo "Environment: ${envName}"
            echo "Groups: ${group}"
            echo "Service: ${service}"

            def groupFilter = ""

            // ----------------------------
            // GROUP + SERVICE FILTER LOGIC
            // ----------------------------

            if(group == "all" && service == "all") {

                // Run everything
                sh """
                mvn clean test \
                -Denv=${envName}
                """

            }
            else if(group == "all") {

                // Run all tests for a specific service
                groupFilter = service

                sh """
                mvn clean test \
                -Denv=${envName} \
                -Dgroups=${groupFilter}
                """

            }
            else if(service == "all") {

                // Run group across all services
                groupFilter = group

                sh """
                mvn clean test \
                -Denv=${envName} \
                -Dgroups=${groupFilter}
                """

            }
            else {

                // Run group within specific service
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