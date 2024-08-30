# Product Service

## Overview

The **Product Service** is a central component of our e-commerce platform, designed to manage all product-related operations. Built using Java with Spring Boot, this service integrates with MongoDB, Redis, and Kafka to handle product data efficiently and support event-driven communication.

## Technology Stack

- **Java Spring Boot**: Framework used for building the microservice with a focus on simplicity and rapid development.
- **MongoDB**: NoSQL database used for persistent storage of product data.
- **Redis**: In-memory data structure store used for caching product data to enhance performance and reduce database load.
- **Kafka**: Distributed event streaming platform used for publishing and subscribing to product-related events, ensuring seamless communication with other services.

## Key Features

### Add Product

- **Functionality**: Adds new products to the catalog.
- **Workflow**:
  1. Validates product information.
  2. Saves the product to MongoDB.
  3. Caches the product in Redis.
  4. Publishes a `ProductAddedEvent` to Kafka.

### Update Product

- **Functionality**: Updates details of existing products.
- **Workflow**:
  1. Validates updated information.
  2. Updates the product in MongoDB.
  3. Updates the cache in Redis.
  4. Publishes a `ProductUpdatedEvent` to Kafka.

### Delete Product

- **Functionality**: Removes products from the catalog.
- **Workflow**:
  1. Checks if the product can be deleted.
  2. Deletes the product from MongoDB.
  3. Removes the product from Redis cache.
  4. Publishes a `ProductDeletedEvent` to Kafka.

### Retrieve Product

- **Functionality**: Fetches product details.
- **Workflow**:
  1. Retrieves product data from Redis cache if available.
  2. If not in cache, fetches from MongoDB and updates the cache.
  
## Scheduling

- **Cache Update**: A scheduled task updates the product cache every 2 hours to ensure data freshness. This is managed using Spring's `@Scheduled` annotation.

## Setup and Configuration

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd product-service
