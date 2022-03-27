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
        stage('Build And Push Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker_repository', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  // available as an env variable, but will be masked if you try to print it out any which way
                  // note: single quotes prevent Groovy interpolation; expansion is by Bourne Shell, which is what you want
                  sh ' mvn jib:build -Djib.to.auth.username=$USERNAME -Djib.to.auth.password=$PASSWORD'
                }
            }
        }
    }
}