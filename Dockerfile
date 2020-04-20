FROM jenkins/jenkins:lts

ENV JENKINS_USER admin
ENV JENKINS_PASS admin

# Skip initial setup
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false


COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY init-pipeline.groovy /usr/share/jenkins/ref/init.groovy.d/init-pipeline.groovy
COPY init-cloud.groovy /usr/share/jenkins/ref/init.groovy.d/init-cloud.groovy
