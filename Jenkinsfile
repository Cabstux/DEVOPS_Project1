pipeline {
    agent any

    environment {
        GITHUB_WEBHOOK_SECRET = credentials('b6d6e53a7fb7d60c9445289a5d968') // Remplacez par la bonne méthode pour récupérer votre secret
    }
    

     stages {

        
        stage('Git Download') {
            steps {
                git branch: 'main', url: 'https://github.com/Cabstux/DEVOPS_Project1.git'
            }
        }
        
        stage('Unit Test') {
            steps {
                sh '/usr/local/bin/mvn test'
            }
        }

        stage('Integration Test') {
            steps {
                sh '/usr/local/bin/mvn verify -DskipUnitTests'
            }
        }
        
        stage('Maven Build') {
            steps {
                sh '/usr/local/bin/mvn clean install'
            }
        }
        
        stage('Static Test Analysis') {
            steps {
                script {
                    //Active static test
                    withSonarQubeEnv(credentialsId: 'token-sonarqube') {
                        sh '/usr/local/bin/mvn clean package sonar:sonar'
                    }
                }
            }
        }
        
        stage('Upload artifact to Nexus') {
            steps {
                script {
                    nexusArtifactUploader artifacts: [[artifactId: 'springboot', classifier: '', 
                    file: 'target/Uber.jar', type: 'jar']], credentialsId: 'auth-nexus', groupId: 'com.example', 
                    nexusUrl: 'localhost:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'DEVOPS-Project1', version: '1.0.1'
                }
            }
        }

        
    }


}
