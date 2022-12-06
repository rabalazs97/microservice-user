pipeline {
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=/var/maven"
    }
    agent {
        docker {
            image "maven:3.8.6-openjdk-18"
            args '-u root -v /home/ci-cd/maven-repo:/var/maven/.m2 -e MAVEN_CONFIG=/var/maven/.m2'
        }
    }

    stages {
        stage("Build") {
            steps {
                sh "mvn -version"
                sh "mvn clean install"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}