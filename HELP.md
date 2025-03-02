# LocationTracker

LocationTracker is a Java-based application that simulates location changes over time, sends them through Kafka, stores them in Redis, and calculates the total distance traveled using the Haversine formula.

## Features
- Generates random location coordinates (`longitude`, `latitude`) every 5 seconds.
- Publishes location data to a Kafka topic (`location-topic`).
- Stores locations in Redis Sorted Sets with timestamps as scores.
- Calculates the total distance traveled between consecutive locations upon each new addition.
- Logs events using SLF4J with detailed debugging output.

## Tech Stack
- **Java**: 21
- **Spring Boot**: 3.2.3
- **Apache Kafka**: For message streaming
- **Redis**: For storing location data
- **Moshi**: For JSON serialization/deserialization
- **Lombok**: To reduce boilerplate code
- **Docker & Docker Compose**: For containerized deployment

## Prerequisites
- **Docker**: Required to run the application with dependencies (Kafka, Zookeeper, Redis).
- **Docker Compose**: To manage multi-container setup.
- **Maven**: To build the project (included in Docker image).

## How to Run
1. **Clone the repository**:
   ```bash
   git clone https://github.com/YourUsername/LocationTracker.git
   cd LocationTracker
   docker-compose up --build app