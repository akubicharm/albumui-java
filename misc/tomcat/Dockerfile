FROM registry.redhat.io/jboss-webserver-7/jws70-openjdk21-openshift-rhel9:7.0.1-3

USER 0


COPY albumui-1.0-SNAPSHOT.war /opt/jws-7.0/tomcat/webapps/albumui.war
#COPY server.xml /opt/jws-7.0/tomcat/conf/server.xml

EXPOSE 8080

CMD ["/opt/jws-7.0/tomcat/bin/catalina.sh", "run"]



