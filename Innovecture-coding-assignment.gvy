//Asumption :
// Assuming that Remote server having Jenkins Public key for SSH authantication.
// Assuming that Developer Pull Request is Merged on Gitlab 
// Assuming that Git remote set as below format
// Ex - https://abcuser:abcuserpasswd@gitlab.com/project_name/app_name.git
// Assuming that we are Building, Testing and Deploying on Live / UAT
// Assuming that we have Project application Directory /home/Ubuntu/webapps/Project_name

def remote = [:]
remote.name = "Ubuntu"
remote.host = "192.168.0.1"
remote.port = 22
remote.allowAnyHosts = true
remote.user = 'Ubuntu'
//identityFile Path Change According to Location on Server for privatekey
remote.identityFile = '/home/jekins/id_rsa.pem'

pipeline {
    agent any 
    stages {
        stage('Project Directory Check') { 
            steps {
                script{
                // In this Stage We are Checking Changes on Server by Dev Team or other Team for Bug testing and we are putting it into  Stash for Reusablity or for Trace. 
                echo 'Checking Project Directory Status...'
                echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
                sshCommand remote: remote, command: ''' 
                    cd /home/Ubuntu/webapps/Project_name;
                    echo '************** Project App **************';
                    if [ 0 -eq `git status --porcelain|wc -l` ]; then 
                        echo ':::::::: No Changes On server ::::::::'; 
                    else 
                      echo ':::::::: Changes Present ::::::::';
                      git status;
                      echo '---------------- Stash ----------';
                      git stash; 
                    fi;
                    '''
                  }
            }
        }
        stage('Storing Last Git Log') { 
            steps {
              //Reason Behind to Printing and maintaining as txt file of git log, Pipeline will Revert a code if any failure/Confilcts accoured during Deployment.
                echo 'Last Gitlog Status checking .....'
                echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
                script {
                        sshCommand remote: remote, command: ''' 
                                  cd /home/Ubuntu/webapps/Project_name;
                                  echo '************** Project App **************';
                                  echo '-------------- Last 3 Git log'
                                  git log --max-count=3 --no-merges;
                                  DATE=`date +%Y%m%d`;
                                  git log --max-count=1 --format=format:%H > /tmp/$DATE-Last-gitlog.txt

                                  '''
                        }
                }
            
        }
        stage('Building Application') { 
            steps {
                echo 'Some of Building Scenarios';
                echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
                script {
                        // Building script on other Machines or Developer Instance
                        }
                }
            
        }
        stage('Testing Application Test Cases') { 
            steps {
                echo 'Some of Testing Test-Cases for SanityCheck';
                echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
                script {
                        // Script for Application Sanity Check 
                        // Like checking Application by Deploying Application in Temporay Container and performing testing
                        // Or third party Plugin Connectivity such as codecy, Travis CI etc.
                        }
                }
            
        }
        stage('Deploying Updated Changes') { 
            steps {
                script {
                    echo 'updating latest codebase .....'
                    echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
                    

                                                sshCommand remote: remote, command: ''' 
                                                cd /home/Ubuntu/webapps/Project_name;
                                                echo '************** Project App **************';
                                                if [ 0 -eq `git status --porcelain|wc -l` ]; then 
                                                    echo ':::::::: No Changes On server ::::::::';
                                                    if [ 0 -eq `git status --porcelain|wc -l` ]; then
                                                     echo "-------->> No Conflicts <<-------";
                                                    else
                                                      echo "--------->> Conflicts Occured <<-------";
                                                      echo "---- Number of Conflicts File status ----";
                                                      git status;
                                                      echo "--------->> Reverting to Old Commit <<-----";
                                                      SHA=`ls -t *-Last-gitlog.txt | head 1`;
                                                      git reset --hard $SHA;
                                                      mail -s "Conflicts Occured ; Changes Reverted from Application" username@gmail.com
                                                    fi; 
                                                else
                                                  echo ':::::::: Changes Present ::::::::';
                                                  git status;
                                                  echo '---------------- Stash ----------';
                                                  git stash; 
                                                  branch=`git rev-parse --abbrev-ref HEAD`;
                                                  echo $branch;
                                                  remotename='git remote'
                                                  git pull $remotename $branch;
                                                  if [ 0 -eq `git status --porcelain|wc -l` ]; then
                                                     echo "-------->> No Conflicts <<-------"; 
                                                  else
                                                    echo "--------->> Conflicts Occured <<-------";
                                                    echo "---- Number of Conflicts File status ----";
                                                    git status;
                                                    echo "--------->> Reverting to Old Commit <<-----";
                                                    SHA=`ls -t *-Last-gitlog.txt | head 1`;
                                                    git reset --hard $SHA;
                                                    mail -s "Conflicts Occured ; Changes Reverted from Application" username@gmail.com
                                                  fi;                                                    
                                                
                                                '''
                }
            }
        }
        stage('Reloading Web Services') { 
            steps {
                        echo "Running ${env.BUILD_ID} ${env.BUILD_DISPLAY_NAME} on ${env.NODE_NAME} and JOB ${env.JOB_NAME}"
                        script {
                            stage('Nginx Syntax Checking') {
                                  echo 'Nginx Syntaxing ..'
                                                sshCommand remote: remote, command: ''' 
                                                sudo nginx -t;
                                                '''
                              }
                              stage('Nginx Service Reloading') {
                                  echo 'Nginx Syntaxing ..'
                                                sshCommand remote: remote, command: ''' 
                                                sudo service nginx reload;
                                                '''
                              }
                              stage('Supervisor Staus Checking') {
                                  echo 'Supervisor Status Checking ..'
                                                sshCommand remote: remote, command: ''' 
                                                sudo supervisorctl status;
                                                '''
                              }
                              stage('Supervisor restarting') {
                                  echo 'Supervisor restarting all gunicorn ..'
                                                sshCommand remote: remote, command: ''' 
                                                sudo supervisorctl restart all;
                                                '''
                              }
                              
                        }   
            }
        }
        
    }
    post {
      //steps {

            success {
              mail bcc: '', body: "<b>URL www.xyz.com is successfully updated with Changes </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} \
              <br> URL build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '',
              subject: "Success to Update www.xyz.com : Project name -> ${env.JOB_NAME}", to: "username@gmail.com";  

            }
            failure {
              mail bcc: '', body: "<b>This Pipeline is Faild to Build Application completely, Please click on URL to understand build failure. </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} \
              <br> URL build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '',
              subject: "ERROR to Build Application www.xyz.com : Project name -> ${env.JOB_NAME}", to: "username@gmail.com";  
            }
            
        
      }
  }
