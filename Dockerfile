FROM openjdk:22-slim

COPY target/monitoring_and_communication-0.0.1-SNAPSHOT.jar ./app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
