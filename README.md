
# Product Service

## Overview

The **Product Service** is a key component of our e-commerce platform, managing product-related operations. Built with Java Spring Boot, it integrates with MongoDB for data storage, Redis for caching, and Kafka for event streaming. The service is containerized using Docker for simplified deployment.

## Technology Stack

- **Java Spring Boot**: Framework for developing the microservice.
- **MongoDB**: NoSQL database for persistent data storage.
- **Redis**: In-memory cache for improved performance.
- **Kafka**: Event streaming platform for inter-service communication.
- **Docker**: Containerization for easy deployment and management.

## Key Features

- **Add Product**: Validate, save, and cache new products; publish `ProductAddedEvent` to Kafka.
- **Update Product**: Update product details, cache updated data, and publish `ProductUpdatedEvent`.
- **Delete Product**: Remove products from database and cache; publish `ProductDeletedEvent`.
- **Retrieve Product**: Fetch product details from cache or database.

## Docker Setup

To deploy the Product Service and its dependencies using Docker, follow these steps:

### Starting Services

1. **Ensure Docker and Docker Compose are installed.**
2. **Navigate to the directory containing your `docker-compose.yml` file.**
3. **Run the following command to build and start the containers:**

   ```bash
   docker-compose up --build


## Running the Product Service

1.  **Verify Service Startup:**
    
    -   Ensure that all services are running correctly. You can check the status of your containers using:
        
		   ```bash
		   docker-compose ps

2.  **Accessing Services:**

	-   **Product Service**: Available at `http://localhost:8082`
	-   **Mongo Express**: Access MongoDB through a web-based interface at `http://localhost:8081`
	-   **Zookeeper**: Manage Kafka service at `localhost:2181`
	-   **Kafka**: Messaging system available at `localhost:9092`


3.  **API Endpoints:**
	 -   **Add Product**: `POST /products`
	-   **Update Product**: `PUT /products/{id}`
	-   **Delete Product**: `DELETE /products/{id}`
	-   **Retrieve Product**: `GET /products/{id}`

4. **Example API Calls:**
-   **Add Product**:
	 
		curl -X POST http://localhost:8082/api/products -H "Content-Type: application/json" -d 
		'{
			"name":"Iphone 15 pro",
			"code":"code5",
			"description":"Mobile Phone",
			"price":2200
		}'

   
-   **Update Product**:
   
		  curl -X PUT http://localhost:8082/api/products/60c72b2f5b4e1c6e3f8d4f75 -H "Content-Type: application/json" -d
		'{
			"name":  "Iphone 12 pro",
			"code":  "code3",
			"description":  "Mobile Phone",
			"price":  2200
		}'

    
-   **Delete Product**:
    
		   curl -X DELETE http://localhost:8082/api/products/60c72b2f5b4e1c6e3f8d4f75

    
-   **Retrieve Product**:
    
		  curl -X GET http://localhost:8082/api/products/60c72b2f5b4e1c6e3f8d4f75
