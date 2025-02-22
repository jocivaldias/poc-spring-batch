# POC Spring Batch Manager Application

This project demonstrates a manager/worker architecture using Java, Spring Boot, and Maven. It uses Spring Batch for partitioning work and Docker Compose for infrastructure components like MySQL and LocalStack (for AWS SQS).

## Prerequisites

- Java 11 or higher
- Maven
- Docker and Docker Compose

## Project Structure

- `src/main/java/`: Java source code
- `pom.xml`: Maven build configuration
- `docker-compose.yml`: Configuration for manager and worker services
- `docker-compose-infra.yml`: Configuration for MySQL and LocalStack

## Building the Application

1. Build the project using Maven:

   ```bash
   mvn clean install
   ```
This creates the application JAR used by both manager and worker containers. 

## Running the Infrastructure Services

Start MySQL and LocalStack:

```bash
docker-compose -f docker-compose-infra.yml up -d
```

This command starts the infrastructure containers on a custom Docker network.

### Running the Application

With infrastructure services up, run the manager and worker services:

```bash
docker-compose up
```

## Environment Variables

Set the following environment variables in the **docker-compose.yml**:

* **SPRING_DATASOURCE_URL**: JDBC URL for MySQL connection
* **AWS_ACCESS_KEY_ID**, AWS_SECRET_ACCESS_KEY, AWS_REGION: AWS credentials for LocalStack
* **LOCALSTACK_URL**: Endpoint URL for LocalStack (e.g., http://localstack:4566)

These variables are configured in the **docker-compose.yml** file.

## Additional Configuration

The application includes a sample implementation for partitioning work using Spring Batch. For example, the BasicPartitioner class divides the work into partitions, and the LoggingStepListener class logs the execution of each step.  The manager service can instruct workers to gracefully shut down by sending a shutdown command via an AWS SQS queue. Workers poll the queue and exit upon receiving the signal.

