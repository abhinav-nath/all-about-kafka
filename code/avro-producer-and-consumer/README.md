# Kafka Avro Producer and Consumer

**Steps to run:**

1. Run all the required components using the docker-compose.yml present in the producer project:

    `docker-compose up -d`

2. Run the producer and consumer apps

3. Send a REST request to the producer to generate a Kafka message, sample request:

    ```shell
    curl -v -X POST 'http://localhost:8098/books' -H 'Content-Type: application/json' --data-raw '{
    "isbn": "1000",
    "title": "Dark Matter 1",
    "author": "Blake Crouch 1"
    }'
    ```

4. Producer will send a message in the **Avro format** and Consumer will receive it and print it on the console
