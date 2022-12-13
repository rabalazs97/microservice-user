pipeline {
    agent any

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
        stage("Integration Tests"){
            steps {
                sh "mvn failsafe:integration-test"
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}