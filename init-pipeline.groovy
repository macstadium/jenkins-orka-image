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
        stage('list files') {
            steps {
                sh 'ls -la'
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
