# Dockerfile
FROM openjdk:21-slim

COPY target/poc-spring-batch-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]