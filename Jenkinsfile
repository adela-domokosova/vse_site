pipeline{
    agent any

    environment{
        IMAGE_NAME = "app_image"
        CONTAINER_NAME = "app-container"
    }

    stages{
    stage('Checkout') {
                steps {
                    git 'https://github.com/adela-domokosova/vse_site.git'
                }
            }

        stage ('Build Stage') {
            steps{
                sh './mvn clean package -DskipTests'
                sh 'ls -lah target/'
            }
        }

        stage('Build Docker Image') {
             steps{
                sh 'docker build -t ${IMAGE_NAME}:latest .'
                }
             }

        stage('Run Docker Container') {
                    steps {
                        sh '''
                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true
                        docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${IMAGE_NAME}:latest
                        '''
                    }
                }
    }
}