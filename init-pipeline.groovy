import jenkins.model.Jenkins

Jenkins.instance.createProjectFromXML("Orka Pipeline", new ByteArrayInputStream("""\
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job">
  <description></description>
  <keepDependencies>false</keepDependencies>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps">
    <script>pipeline {
    agent { label &apos;orka&apos; }
    stages {
        stage(&apos;install&apos;) {
            steps {
                sh &apos;echo Installing dependencies!&apos;
            }
        }
        stage(&apos;build&apos;) {
            steps {
                sh &apos;echo Building on: \$(uname -a)&apos;
            }
        }
         stage(&apos;test&apos;) {
            steps {
                sh &apos;echo Testing!&apos;
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
