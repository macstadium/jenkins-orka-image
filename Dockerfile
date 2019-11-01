FROM jenkins/jenkins:lts
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY init-pipeline.groovy /usr/share/jenkins/ref/init.groovy.d/init-pipeline.groovy
COPY init-cloud.groovy /usr/share/jenkins/ref/init.groovy.d/init-cloud.groovy
