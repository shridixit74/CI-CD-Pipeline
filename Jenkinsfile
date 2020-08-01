node{
    stage("Git / SCM Checkout ..."){
        git credentialsId: 'gihubcredis74', url: 'https://github.com/shridixit74/CI-CD-Pipeline.git'

    }
    stage('Maven Package installation ...'){
	def mvn_home = tool name: 'maven3.3.9', type: 'maven'
	def mvn_cmd  = "${mvn_home}/bin/"
	sh label: 'Maven Cleaning...', script: "${mvn_cmd}mvn clean package"
    }
    stage('Build Docker Image ...'){
	sh 'docker build -t shrikant9867/my_app:1.0.0 .'
    }
    stage('Updating Docker Repo...'){
	withCredentials([string(credentialsId: 'DockerPasswd', variable: 'DockerPasswd')]) {
			sh "docker login -u shrikant9867 -p ${DockerPasswd} docker.io"
			sh "docker tag shrikant9867/my_app:1.0.0 shrikant9867/project-application:1.0.0"
			sh "docker push shrikant9867/project-application:1.0.0"
		}
	stage('Deploying On AWS Server ...'){
		def old_docker_stop = 'docker stop project-application'
		def old_docker_remove = 'docker rm -f project-application'
		def old_docker_img_rm = 'docker image rm -f shrikant9867/project-application:1.0.0'
		def dockerRun = 'docker run -p 8080:8080 -d --name project-application shrikant9867/project-application:1.0.0'
		sshagent(['AWSEC-Ubuntu']) {
			try{
				sh "ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 ${old_docker_stop}"
				sh "ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 ${old_docker_remove}"
				sh "ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 ${old_docker_img_rm}"
				sh "ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 ${dockerRun}"
			}catch(Exception ex){
				println("Exception to Execute:"+ex)
			}
				
					
		}	
	 }	
	
    }

}

