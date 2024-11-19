FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/storage-service-0.0.1-SNAPSHOT.jar /app
RUN mkdir -p /app/data/posts
EXPOSE 9000
CMD ["java", "-jar", "storage-service-0.0.1-SNAPSHOT.jar"]

