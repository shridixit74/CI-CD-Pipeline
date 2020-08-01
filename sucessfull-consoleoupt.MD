Started by user shrikant Dixit
Running in Durability level: MAX_SURVIVABILITY
[Pipeline] Start of Pipeline (hide)
[Pipeline] node
Running on Jenkins in /var/lib/jenkins/workspace/Docker-App-Deploy
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Git / SCM Checkout ...)
[Pipeline] git
using credential gihubcredis74
 > git rev-parse --is-inside-work-tree # timeout=10
Fetching changes from the remote Git repository
 > git config remote.origin.url https://github.com/shridixit74/CI-CD-Pipeline.git # timeout=10
Fetching upstream changes from https://github.com/shridixit74/CI-CD-Pipeline.git
 > git --version # timeout=10
 > git --version # 'git version 2.7.4'
using GIT_ASKPASS to set credentials github access of repo
 > git fetch --tags --progress https://github.com/shridixit74/CI-CD-Pipeline.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/master^{commit} # timeout=10
 > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10
Checking out Revision 4fce8db14a09e84a1affd6c35cf7b32d11887a3f (refs/remotes/origin/master)
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 4fce8db14a09e84a1affd6c35cf7b32d11887a3f # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git branch -D master # timeout=10
 > git checkout -b master 4fce8db14a09e84a1affd6c35cf7b32d11887a3f # timeout=10
Commit message: "[Feature] - #00000 |  Pipeline Designed for www.xyz.com web application"
 > git rev-list --no-walk 4fce8db14a09e84a1affd6c35cf7b32d11887a3f # timeout=10
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Maven Package installation ...)
[Pipeline] tool
[Pipeline] sh (Maven Cleaning...)
+ /opt/maven/bin/mvn clean package
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building my-app 0.0.1
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ myweb ---
[INFO] Deleting /var/lib/jenkins/workspace/Docker-App-Deploy/target
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ myweb ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /var/lib/jenkins/workspace/Docker-App-Deploy/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.6.1:compile (default-compile) @ myweb ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 1 source file to /var/lib/jenkins/workspace/Docker-App-Deploy/target/classes
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ myweb ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /var/lib/jenkins/workspace/Docker-App-Deploy/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.6.1:testCompile (default-testCompile) @ myweb ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 1 source file to /var/lib/jenkins/workspace/Docker-App-Deploy/target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:2.19:test (default-test) @ myweb ---

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running in.javahome.myweb.controller.CalculatorTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 sec - in in.javahome.myweb.controller.CalculatorTest
Results :

Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

[INFO] 
[INFO] --- maven-war-plugin:2.2:war (default-war) @ myweb ---
[INFO] Packaging webapp
[INFO] Assembling webapp [myweb] in [/var/lib/jenkins/workspace/Docker-App-Deploy/target/myweb-0.0.1]
[INFO] Processing war project
[INFO] Copying webapp resources [/var/lib/jenkins/workspace/Docker-App-Deploy/src/main/webapp]
[INFO] Webapp assembled in [43 msecs]
[INFO] Building war: /var/lib/jenkins/workspace/Docker-App-Deploy/target/myweb-0.0.1.war
[INFO] WEB-INF/web.xml already added, skipping
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.545 s
[INFO] Finished at: 2020-08-01T18:32:17+05:30
[INFO] Final Memory: 18M/198M
[INFO] ------------------------------------------------------------------------
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Build Docker Image ...)
[Pipeline] sh
+ docker build -t shrikant9867/my_app:1.0.0 .
Sending build context to Docker daemon  6.721MB

Step 1/2 : FROM tomcat:8
 ---> c142fa2ea808
Step 2/2 : COPY target/*.war /usr/local/tomcat/webapps/myweb.war
 ---> 31e6baf2983f
Successfully built 31e6baf2983f
Successfully tagged shrikant9867/my_app:1.0.0
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Updating Docker Repo...)
[Pipeline] withCredentials
Masking supported pattern matches of $DockerPasswd
[Pipeline] {
[Pipeline] sh
+ docker login -u shrikant9867 -p **** docker.io
WARNING! Using --password via the CLI is insecure. Use --password-stdin.
WARNING! Your password will be stored unencrypted in /var/lib/jenkins/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
[Pipeline] sh
+ docker tag shrikant9867/my_app:1.0.0 shrikant9867/project-application:1.0.0
[Pipeline] sh
+ docker push shrikant9867/project-application:1.0.0
The push refers to repository [docker.io/shrikant9867/project-application]
619aea9d223b: Preparing
c00bf10b2eab: Preparing
8ddc561c5b86: Preparing
9c70c4e7cd3b: Preparing
58d5d06696be: Preparing
99ca731afd41: Preparing
4c2b0eaf4b81: Preparing
46a297e68c47: Preparing
cf47dfabe081: Preparing
c53d956ebfec: Preparing
6086e1b289d9: Preparing
46a297e68c47: Waiting
cf47dfabe081: Waiting
c53d956ebfec: Waiting
6086e1b289d9: Waiting
99ca731afd41: Waiting
4c2b0eaf4b81: Waiting
58d5d06696be: Layer already exists
9c70c4e7cd3b: Layer already exists
8ddc561c5b86: Layer already exists
c00bf10b2eab: Layer already exists
99ca731afd41: Layer already exists
46a297e68c47: Layer already exists
cf47dfabe081: Layer already exists
4c2b0eaf4b81: Layer already exists
c53d956ebfec: Layer already exists
6086e1b289d9: Layer already exists
619aea9d223b: Pushed
1.0.0: digest: sha256:1b963d3d7abef75cbb4f4a523efd6e3c693dc7a81ca69c0e3ca122d1bfe35100 size: 2632
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] stage
[Pipeline] { (Deploying On AWS Server ...)
[Pipeline] sshagent
[ssh-agent] Using credentials ubuntu (AWSEC-Ubuntu)
[ssh-agent] Looking for ssh-agent implementation...
[ssh-agent]   Exec ssh-agent (binary ssh-agent on a remote machine)
$ ssh-agent
SSH_AUTH_SOCK=/tmp/ssh-s6wDybFazOMN/agent.8795
SSH_AGENT_PID=8798
Running ssh-add (command line suppressed)
Identity added: /var/lib/jenkins/workspace/Docker-App-Deploy@tmp/private_key_1996017573059816859.key (/var/lib/jenkins/workspace/Docker-App-Deploy@tmp/private_key_1996017573059816859.key)
[ssh-agent] Started.
[Pipeline] {
[Pipeline] sh
+ ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 docker stop project-application
project-application
[Pipeline] sh
+ ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 docker rm -f project-application
project-application
[Pipeline] sh
+ ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 docker image rm -f shrikant9867/project-application:1.0.0
Untagged: shrikant9867/project-application:1.0.0
Untagged: shrikant9867/project-application@sha256:bbb1015b126772e6cb3dedec6e77204f1887640209947caf7df31b6f0154f80f
Deleted: sha256:57433fa9df4f8ef944fdfd08d142e9f63de2e8efbcacd32ecf8cba354365f4cf
Deleted: sha256:d8749d9a542ca61b0ef7edcf678e42a668cd04b682519f41ef63aa773a4b32bb
Deleted: sha256:f05deac807a25a1adb04a5622031b3e3ab44cab0b46eae6071f862d7f6e62c08
Deleted: sha256:b4802d654653603a3bf56e3eac97168162132e2fe7e63c70b70e5b983fc185d4
Deleted: sha256:5d98f8bd04c20fd2a30760e70fb50612b1e310ced6934474fe42d0553df37c24
Deleted: sha256:496d99b78d8232a2ac51f25d5b115f5d378f07acb88c63a2581441709f77b39f
Deleted: sha256:b08fe0d7b0c261ee720f02ff8e42f7933d413cb3130a1ee647320eff9c9f3163
Deleted: sha256:240796db5290a8db1a190841d7f27566f9fed869c5b06023038a80892dae3a8e
Deleted: sha256:ea946ee317881137f6a742a039827bfc44bf7868326911f1f614ce2064ae935a
Deleted: sha256:88449201ebb3fe413168e1cdb4ba51cc3ff956415f37ce5d98da3e2cbbe582bf
Deleted: sha256:0ed790126387ca1c9e8ba10d5afeb1f8501d692288bf6d3b3dc1a5dbfb69edec
Deleted: sha256:6086e1b289d997dfd19df1ec9366541c49f5545520f9dc65ebd4cd64071497b4
[Pipeline] sh
+ ssh -o StrictHostKeyChecking=no ubuntu@52.66.209.206 docker run -p 8080:8080 -d --name project-application shrikant9867/project-application:1.0.0
Unable to find image 'shrikant9867/project-application:1.0.0' locally
1.0.0: Pulling from shrikant9867/project-application
31dd5ebca5ef: Pulling fs layer
3ed641c4ae98: Pulling fs layer
bcd57146431e: Pulling fs layer
ac34a4d7c330: Pulling fs layer
d29274a3f575: Pulling fs layer
1b90ee88ecd5: Pulling fs layer
9f9d29dec66c: Pulling fs layer
2253269f1b6e: Pulling fs layer
f21a2a8111ad: Pulling fs layer
8cf737bb6167: Pulling fs layer
fa5244055b9f: Pulling fs layer
ac34a4d7c330: Waiting
d29274a3f575: Waiting
1b90ee88ecd5: Waiting
9f9d29dec66c: Waiting
2253269f1b6e: Waiting
f21a2a8111ad: Waiting
8cf737bb6167: Waiting
fa5244055b9f: Waiting
3ed641c4ae98: Verifying Checksum
3ed641c4ae98: Download complete
bcd57146431e: Verifying Checksum
bcd57146431e: Download complete
d29274a3f575: Verifying Checksum
d29274a3f575: Download complete
31dd5ebca5ef: Verifying Checksum
31dd5ebca5ef: Download complete
1b90ee88ecd5: Verifying Checksum
1b90ee88ecd5: Download complete
ac34a4d7c330: Verifying Checksum
ac34a4d7c330: Download complete
2253269f1b6e: Verifying Checksum
2253269f1b6e: Download complete
8cf737bb6167: Verifying Checksum
8cf737bb6167: Download complete
f21a2a8111ad: Verifying Checksum
f21a2a8111ad: Download complete
31dd5ebca5ef: Pull complete
3ed641c4ae98: Pull complete
bcd57146431e: Pull complete
9f9d29dec66c: Verifying Checksum
9f9d29dec66c: Download complete
fa5244055b9f: Verifying Checksum
fa5244055b9f: Download complete
ac34a4d7c330: Pull complete
d29274a3f575: Pull complete
1b90ee88ecd5: Pull complete
9f9d29dec66c: Pull complete
2253269f1b6e: Pull complete
f21a2a8111ad: Pull complete
8cf737bb6167: Pull complete
fa5244055b9f: Pull complete
Digest: sha256:1b963d3d7abef75cbb4f4a523efd6e3c693dc7a81ca69c0e3ca122d1bfe35100
Status: Downloaded newer image for shrikant9867/project-application:1.0.0
fbbc39a9f20fdaa9512d56ef0c8f9e26c5c1971a8b6800ac5f18d5e9443c07f5
[Pipeline] }
$ ssh-agent -k
unset SSH_AUTH_SOCK;
unset SSH_AGENT_PID;
echo Agent pid 8798 killed;
[ssh-agent] Stopped.
[Pipeline] // sshagent
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
