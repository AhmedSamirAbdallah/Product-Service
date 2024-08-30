# Product Service

## Overview

The Product Service is a crucial component of our e-commerce platform. It manages all operations related to products within the catalog, including adding new products, updating existing products, deleting products, and retrieving product information.

## Key Functions

### 1. Add Product

**Purpose**: Enable the addition of new products to the catalog.

**Business Flow**:
1. Receive a request to add a new product.
2. Validate the product information (e.g., check for unique product name, ensure all required fields are populated).
3. Persist the product data to the database.
4. Publish a `ProductAddedEvent` to Kafka to notify other services (like Inventory and Order services) about the new product.

**Constraints**:
- **Unique Name**: The product name must be unique within the catalog.
- **Mandatory Fields**: Fields such as name, price, category, and SKU (Stock Keeping Unit) are mandatory.

### 2. Update Product

**Purpose**: Modify the details of an existing product.

**Business Flow**:
1. Receive a request to update a product.
2. Validate the updates (e.g., ensure the updated name is still unique).
3. Persist the changes to the database.
4. Publish a `ProductUpdatedEvent` to Kafka to notify other services of the changes.

**Constraints**:
- **Unique Name**: The updated product name must remain unique.
- **Immutable Fields**: Certain fields, like product ID, should not be modifiable.

### 3. Delete Product

**Purpose**: Remove a product from the catalog.

**Business Flow**:
1. Receive a request to delete a product.
2. Verify if the product can be deleted (e.g., ensure it’s not part of any active orders).
3. Remove the product from the database.
4. Publish a `ProductDeletedEvent` to Kafka to notify other services of the deletion.

**Constraints**:
- **Data Integrity**: Ensure that the deletion doesn’t violate any referential integrity, such as products that are still in active orders.

### 4. Retrieve Product

**Purpose**: Fetch product details.

**Business Flow**:
1. Receive a request to retrieve product information.
2. Query the database for the product details.
3. Return the product details to the requester.

**Constraints**:
- **Search Flexibility**: Support various query parameters like product ID, name, category, etc.

## Integration

### Kafka

- **Event Publishing**: When a product is added, updated, or deleted, corresponding events (`ProductAddedEvent`, `ProductUpdatedEvent`, `ProductDeletedEvent`) are published to Kafka. This ensures that other services like Inventory and Order Services are informed about changes to the product catalog.

### API Gateway

- **Endpoint Exposure**: The Product Service exposes endpoints through the API Gateway for client applications to interact with the product catalog. Integration with the API Gateway allows external systems and users to access the product catalog functionalities through a unified entry point.

## Setup and Configuration

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd product-service
