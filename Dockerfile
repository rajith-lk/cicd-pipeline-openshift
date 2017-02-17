FROM registry.access.redhat.com/jboss-eap-6/eap64-openshift

EXPOSE 8080 9990 9999
CMD ["/opt/eap/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

COPY target/cicd.openshift-1.0.war $JBOSS_HOME/standalone/deployments/
USER root
RUN chown jboss:jboss $JBOSS_HOME/standalone/deployments/cicd.openshift-1.0.war
USER jboss

