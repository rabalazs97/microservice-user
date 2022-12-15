pipeline {
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=/var/maven"
    }
    agent {
        docker {
            image "maven:3.8.6-openjdk-18"
            args '-u root --net=host -v /home/ci-cd/maven-repo:/var/maven/.m2 -v /var/run/docker.sock:/var/run/docker.sock -e MAVEN_CONFIG=/var/maven/.m2'
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
        stage("Integration Tests"){
            steps {
                sh "mvn failsafe:integration-test"
            }
        }
        stage("Build image and push to repo"){
            steps {
                sh "mvn spring-boot:build-image"
                sh "docker tag user:0.0.1-SNAPSHOT rabalazs97/user:latest"
                sh "docker push rabalazs97/user:latest"
                sh "docker rmi user:0.0.1-SNAPSHOT rabalazs97/user:latest"
            }
        }
    }
    post {
        success {
            build job: 'Deploy'
        }
        always {
            cleanWs()
        }
    }
}