FROM openjdk:latest
MAINTAINER Imperial DevOps Project <rrr2417@ic.ac.uk>

RUN apt-get update && apt-get install -y -q \
    maven \
    pandoc 	

RUN mkdir /simplewebapp
COPY pom.xml system.properties /simplewebapp/
COPY src /simplewebapp/src/

WORKDIR /simplewebapp
RUN mvn package
ENV PORT 5000

CMD ["sh", "target/bin/simplewebapp"]
