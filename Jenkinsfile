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
        stage("Package") {
            steps {
                sh "mvn -version"
                sh "mvn clean package -DskipTests"
            }
        }
        stage("Unit Tests"){
            steps {
                sh "mvn test"
            }
        }
    }
    post {
        success {
            archiveArtifacts artifacts: '**/*.jar'
        }
        always {
            cleanWs()
        }
    }
}