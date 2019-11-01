import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import hudson.model.Node.Mode
import io.jenkins.plugins.orka.OrkaCloud
import io.jenkins.plugins.orka.AgentTemplate
import io.jenkins.plugins.orka.RunOnceCloudRetentionStrategy
import java.util.ArrayList
import jenkins.model.Jenkins

String credentialsId = java.util.UUID.randomUUID().toString()
Credentials sshCredentials = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, credentialsId, "VM SSH credentials", "admin", "admin")
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), sshCredentials)

AgentTemplate template = new AgentTemplate(credentialsId, "orkademo", false, null, null, 0, 1, "/Users/admin", Mode.NORMAL, "orka", new RunOnceCloudRetentionStrategy(5), null)
ArrayList<AgentTemplate> templates = new ArrayList<AgentTemplate>()
templates.add(template)

OrkaCloud cloud = new OrkaCloud("Orka Cloud", null, System.getenv()['ORKA_ENDPOINT'], templates)

Jenkins.instance.clouds.add(cloud)
Jenkins.instance.save()
