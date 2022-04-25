# Kafka Avro Producer and Consumer

Steps to run:

1. Run all the required components using the docker-compose.yml present in the producer project:

    `docker-compose up -d`

2. Run the producer and consumer apps

3. Send a REST request to the producer to generate a Kafka message, sample payload:

    ```json
    {
      "isbn": "1000",
      "title": "Dark Matter",
      "author": "Blake Crouch"
    }
    ```

4. Producer will send a message in the **Avro format** and Consumer will receive it and print it on the console
