FROM beta-distribution-base:latest as builder

FROM maven:3.6.3-jdk-8

WORKDIR /opt/beta-distribution

COPY --from=builder /opt/beta-distribution/target/beta-distribution-0.0.1-SNAPSHOT-jar-with-dependencies.jar /opt/beta-distribution/target/beta-distribution-0.0.1-SNAPSHOT-jar-with-dependencies.jar

CMD ["java",  "-jar", "/opt/beta-distribution/target/beta-distribution-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]
