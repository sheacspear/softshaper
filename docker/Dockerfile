FROM       tomcat:8.5
MAINTAINER SoftShaper <ashek@softshaper.org>

#  External volume definitions
RUN rm -r /usr/local/tomcat/webapps/ROOT
ADD zorb-mini-0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
