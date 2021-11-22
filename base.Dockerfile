FROM maven:3.6.3-jdk-8

WORKDIR /opt/beta-distribution

# Add and Install Application Code.
COPY pom.xml /opt/beta-distribution
COPY src /opt/beta-distribution/src

RUN mvn package
