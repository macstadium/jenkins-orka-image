import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import hudson.model.AdministrativeMonitor
import hudson.model.Node.Mode
import io.jenkins.plugins.orka.OrkaCloud
import io.jenkins.plugins.orka.AgentTemplate
import io.jenkins.plugins.orka.RunOnceCloudRetentionStrategy
import java.util.ArrayList
import java.util.Collections
import jenkins.model.Jenkins

String orkaEndpoint = System.getenv()['ORKA_ENDPOINT'] ?: "http://10.221.188.100";

String orkaUsername = System.getenv()['ORKA_USERNAME'];
String orkaPassword = System.getenv()['ORKA_PASSWORD'];
String sshUsername =  System.getenv()['SSH_USERNAME'] ?: "admin"
String sshPassword =  System.getenv()['SSH_PASSWORD'] ?: "admin"

String vmConfigName = System.getenv()['VM_CONFIG_NAME'] ?: "orka-jenkins"
String vmBaseImage = System.getenv()['VM_BASE_IMAGE'];
String vmCpuCountString = System.getenv()['VM_CPU_COUNT'] ?: 3
int vmCpuCount = Integer.parseInt(vmCpuCountString);
String agentLabel = System.getenv()['AGENT_LABEL'] ?: "orka"

String remoteFs = System.getenv()['REMOTE_FS_ROOT'] ?: "/Users/admin"

String sshUserCredentialsId = java.util.UUID.randomUUID().toString()
String orkaUserCredentialsId = java.util.UUID.randomUUID().toString()

Credentials sshCredentials = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, sshUserCredentialsId, "VM SSH credentials", sshUsername, sshPassword)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), sshCredentials)

Credentials orkaCredentials = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, orkaUserCredentialsId, "Orka credentials", orkaUsername, orkaPassword)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), orkaCredentials)

AgentTemplate template = new AgentTemplate(sshUserCredentialsId, null, true, vmConfigName, 
    vmBaseImage, vmCpuCount, 1, remoteFs, 
    Mode.NORMAL, agentLabel, new RunOnceCloudRetentionStrategy(5), Collections.emptyList());

ArrayList<AgentTemplate> templates = new ArrayList<AgentTemplate>()
templates.add(template)

OrkaCloud cloud = new OrkaCloud("Orka Cloud", orkaUserCredentialsId, orkaEndpoint, null, templates)

Jenkins.instance.clouds.add(cloud)

Jenkins.instance.administrativeMonitors.each { x-> 
    String name = x.getClass().name
    if (name.contains("SecurityIsOffMonitor") ||
        name.contains("SecurityIsOffMonitor") ||
        name.contains("URICheckEncodingMonitor") ||
        name.contains("UpdateCenter") ||
        name.contains("UpdateSiteWarningsMonitor") ||
        name.contains("RootUrlNotSetMonitor") ||
        name.contains("CSRFAdministrativeMonitor")) {
        x.disable(true)
    }
}

Jenkins.instance.save()
