pipeline {
    agent any
    tools {
        maven 'maven 3'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Deploy') {
            steps {
                withEnv(['JENKINS_NODE_COOKIE=dontKillMe']){
                    sh 'nohup java -jar -DServer.port=8001 target/employee-management-system-api-0.0.1-SNAPSHOT.jar &'
                }
            }
        }
    }
}