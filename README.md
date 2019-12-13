# Kafka Demo
This is a simple Kafka application in SpringBoot. It is a basic shell used to get started.


## Getting Started

1. Start up Kafka
   ```
   docker-compose up
   ```
1. Start SpringBoot


## Produce Data

To send some data to Kafka, go to this URL.

Default  
curl -X POST http://localhost:8080/produce/default?count=10

Batch  
curl -X POST http://localhost:8080/produce/batch?count=10

