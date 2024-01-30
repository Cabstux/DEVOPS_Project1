pipeline {
    agent any

    triggers {
        // Utilisation du Pipeline Webhook Trigger
        webhook {
            triggerOnPush: true
        }
    }
    stages {
        stage('Webhook Verification') {
            steps {
                script {
                    def githubWebhookSecret = 'b6d6e53a7fb7d60c9445289a5d968'  // Remplacez par votre propre secret

                    // Récupérer le corps de la demande webhook
                    def payload = currentBuild.rawBuild.getEnvVars()['payload']

                    // Récupérer la signature HMAC-SHA1 de l'en-tête
                    def signature = currentBuild.rawBuild.getEnvVars()['HTTP_X_HUB_SIGNATURE']

                    // Vérifier la signature
                    def isVerified = verifyHmacSha1Signature(payload, signature, githubWebhookSecret)

                    if (isVerified) {
                        echo 'Webhook verification successful.'
                    } else {
                        error 'Webhook verification failed. Rejecting the request.'
                    }
                }
            }
        }


    stages {
        stage('Git Download') {
            steps {
                git  branch: 'main', url: 'https://github.com/Cabstux/DEVOPS_Project1.git'
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
                    /* groovylint-disable-next-line NestedBlockDepth */
                    withSonarQubeEnv(credentialsId: 'token-sonarqube') {
                        sh '/usr/local/bin/mvn clean package sonar:sonar'
                    }
                }
            }
        }
        
        stage('Upload artifact to Nexus') {
            steps {
                script {
                    /* groovylint-disable-next-line NestedBlockDepth */
                    nexusArtifactUploader artifacts: [[artifactId: 'springboot', classifier: '', 
                    file: 'target/Uber.jar', type: 'jar']], credentialsId: 'auth-nexus', groupId: 'com.example', 
                    nexusUrl: 'localhost:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'DEVOPS-Project1', version: '1.0.1'
                }
            }
            def verifyHmacSha1Signature(payload, signature, secret) {
    def mac = javax.crypto.Mac.getInstance('HmacSHA1')
    mac.init(new javax.crypto.spec.SecretKeySpec(secret.bytes, 'HmacSHA1'))
    def hmac = mac.doFinal(payload.bytes)

    def computedSignature = 'sha1=' + hmac.encodeHex().toString()

    return signature == computedSignature
        }
    }
}
