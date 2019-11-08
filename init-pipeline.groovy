import jenkins.model.Jenkins

Jenkins.instance.createProjectFromXML("Orka Pipeline", new ByteArrayInputStream("""\
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job">
  <description></description>
  <keepDependencies>false</keepDependencies>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps">
    <script>pipeline {
    agent { label 'orka' }
    stages {
        stage('clone') {
            steps {
                git 'https://github.com/ispasov/swift-game.git'
            }
        }
        stage('build') {
            steps {
                sh 'xcodebuild -sdk iphonesimulator'
            }
        }
         stage('archive') {
            steps {
                dir('build') {
                    sh 'zip -r simulator-app.zip Release-iphonesimulator/Swift2048.app'
                    archiveArtifacts 'simulator-app.zip'
                }
            }
        }
    }
}
</script>
    <sandbox>true</sandbox>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>
""".bytes))
