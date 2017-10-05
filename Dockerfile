FROM ubuntu:16.04
MAINTAINER alban.gaignard@univ-nantes.fr

ENV GALAXYLD=https://github.com/albangaignard/galaxy-PROV

RUN apt-get -q update && DEBIAN_FRONTEND=noninteractive apt-get -yq --no-install-recommends install git openjdk-8-jre openjdk-8-jdk maven 
RUN git config --global http.sslVerify false
RUN git clone $GALAXYLD
WORKDIR /galaxy-PROV/galaxy-PROV
RUN mvn clean install -Dmaven.test.skip=true
CMD ["java", "-jar","./target/galaxy-PROV-1.0-SNAPSHOT-standalone.jar"]
