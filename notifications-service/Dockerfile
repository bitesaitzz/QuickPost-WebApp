FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/notifications-service-0.0.1-SNAPSHOT.jar /app
EXPOSE 11000
CMD ["java", "-jar", "notifications-service-0.0.1-SNAPSHOT.jar"]
