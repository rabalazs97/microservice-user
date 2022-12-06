pipeline {
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=/var/maven"
    }
    agent none

    stages {
        stage("Fix the permission issue") {

            agent any

            steps {
                sh "sudo chown root:jenkins /run/docker.sock"
            }
        }

        stage("Build") {
            agent {
                docker {
                    image "maven:3.8.6-openjdk-18"
                    args '-v /home/ci-cd/maven-repo:/var/maven/.m2 -e MAVEN_CONFIG=/var/maven/.m2'
                }
            }
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