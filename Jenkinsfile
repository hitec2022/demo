pipeline {
    agent { 
        kubernetes {
            yamlFile 'k8s-builder.yaml'
        } 
    }
    stages {
        stage('BUILD') {
            steps {
                container('maven') {
                    sh 'mvn -DskipTests package'
                }
            }
        }
        
        stage('Dockernizer') {
          steps {
                container('docker') {
                    sh 'docker build --build-arg APP_FILE=target/*.jar -t kind-registry:5000/board:0.0.1 .'
                }
            }
        }
        stage('Docker Push') {
            steps {
                container('docker') {
                    sh 'docker push kind-registry:5000/board:0.0.1'
                }
            }
        }
        stage('Deployment') {
            steps {
                container('kubectl') {
                    //sh 'kubectl apply -f ./k8s/gw-deployment.yaml'
                    sh 'kubectl rollout restart deployment board'
                }
            }
        }
    }
}
