# Product Service

## Overview

The **Product Service** is a core component of our e-commerce platform, responsible for managing product-related operations. Built with Java Spring Boot, it uses MongoDB for data storage, Redis for caching, and Kafka for event streaming. The service is containerized using Docker for easy deployment.

## Technology Stack

- **Java Spring Boot**: Framework for building the microservice.
- **MongoDB**: NoSQL database for persistent storage.
- **Redis**: In-memory cache to enhance performance.
- **Kafka**: Event streaming platform for inter-service communication.
- **Docker**: Containerization for easy deployment and management.

## Key Features

- **Add Product**: Validates, saves, and caches new products. Publishes `ProductAddedEvent` to Kafka.
- **Update Product**: Updates product details, caches the updated data, and publishes `ProductUpdatedEvent`.
- **Delete Product**: Removes products from the database and cache, publishes `ProductDeletedEvent`.
- **Retrieve Product**: Fetches product details from cache or database.

## Docker Setup

To run the Product Service and its dependencies using Docker, follow these steps:

### Docker Compose Configuration

The Docker Compose configuration sets up all necessary services:

```yaml
version: '3.8'

services:
  zookeeper:
    image: zookeeper:3.5
    container_name: zookeeper
    ports:
      - "2181:2181"
    networks:
      - kafka-network

  kafka:
    image: confluentinc/cp-kafka:latest
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    depends_on:
      - zookeeper
    networks:
      - kafka-network

  mongodb:
    image: mongo:latest
    container_name: mongo-db
    ports:
      - "27017:27017"
    networks:
      - mongo-network

  mongo-express:
    image: mongo-express:latest
    container_name: mongo-express
    ports:
      - "8081:8081"
    environment:
      - ME_CONFIG_MONGODB_SERVER=mongo-db
      - ME_CONFIG_BASICAUTH_USERNAME=admin
      - ME_CONFIG_BASICAUTH_PASSWORD=admin
    depends_on:
      - mongodb
    networks:
      - mongo-network

  redis:
    image: redis:latest
    container_name: redis
    ports:
      - "6379:6379"
    networks:
      - redis-network

networks:
    kafka-network:
      driver: bridge
    mongo-network:
      driver: bridge
    redis-network:
      driver: bridge
