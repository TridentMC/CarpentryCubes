pipeline {
	agent any
	stages {
		stage('Build') {
			steps {
				checkout scm
				sh 'rm -f private.gradle'
				sh './gradlew setupCiWorkspace clean build'
				archive 'build/libs/*jar'
			}
		}
		stage('Deploy') {
			steps {
				withCredentials([file(credentialsId: 'tridev-nexus', variable: 'PRIVATEGRADLE')]) {
					sh '''
						cp "$PRIVATEGRADLE" private.gradle
						./gradlew uploadShadow
					'''
				}
			}
		}
	}
}
