pipeline {
    agent any

    stages {
        stage('Build App') {
            steps {
                // making sure mvnw is executable
                sh('chmod +x mvnw')
                sh('./mvnw clean')
                sh('./mvnw package')
            }
        }
        stage('Containerize App') {
            steps {
                sh('docker build -t ezzy-buy-backend:latest .')
            }
            post {
                always {
                    sh('rm -r target/*')
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    try {
                        sh('docker container stop ezzy-buy-backend-app')
                        sh('docker container rm ezzy-buy-backend-app')
                    } catch (Exception e) {
                        echo 'No previous containers'
                    }
                    sh('docker run --env-file /var/lib/jenkins/secrets/.env -itd -p 8081:8443 --name ezzy-buy-backend-app ezzy-buy-backend:latest')
                }
            }
        }
    }
}
