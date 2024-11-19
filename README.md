# **QuickPost Application**

QuickPost is a microservices-based web application that replicates key features of Twitter, leveraging modern technologies for scalability and performance.

---

## **Technologies**

- **Backend**:
  - **Java Spring Boot**: Core framework for building services.
  - **PostgreSQL**: Database for user and message data.
  - **Kafka**: Message broker for communication between services.
  - **Redis**: Caching layer to improve performance.

- **Microservices**:
  - **AppService**: Handles user authentication, message creation, and role management.
  - **NotificationService**: Sends email to user.
  - **StorageService**: Manages file uploads and retrieval.

- **Deployment**:
  - **Docker & Docker Compose**: Containerized services for easy deployment and scalability.

---

## **Quick Start**

1. **For each service, build the application to generate the JAR file using Maven:**:
```bash
./mvnw package -DskipTests
```
2. **After building the JAR files, create Docker images for each service:**
```bash
docker build -t app-service:1.0 ./AppService
docker build -t notification-service:1.0 ./NotificationService
docker build -t storage-service:1.0 ./StorageService
```
3. **Run the Application with Docker Compose(Directory QuickPost)**
```bash
docker-compose up
```

Web-App is working on localhost:10000.

  
   
