pipeline {
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=/var/maven"
    }
    agent {
        docker {
            image "maven:3.8.1-openjdk-17-slim"
            args '-u root --net=host -v /home/ci-cd/maven-repo:/var/maven/.m2 -v /var/run/docker.sock:/var/run/docker.sock -e MAVEN_CONFIG=/var/maven/.m2'
        }
    }
    stages {
        stage("Build") {
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
        stage("Build image and push to repo"){
            steps {
                sh "mvn spring-boot:build-image -DskipTests"
            }
        }
    }
    post {
        success {
            build job: 'deploy-user'
        }
        always {
            cleanWs()
        }
    }
}
