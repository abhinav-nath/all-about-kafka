# Spring Kafka Producer and Consumer

**Steps to run:**

1. Run all the required components using the `docker-compose.yml`

   `docker-compose up -d`

2. Run the producer and consumer apps

3. Send a REST request to the producer to generate a Kafka message, sample request:

   ```shell
   curl -v http://localhost:8089/message -X POST -H "Content-Type:application/json" -d '{"id":"1","text":"message 1"}'
   ```

4. Consumer will receive the message and log it:

   ```shell
   2022-07-26 00:26:43.493  INFO 15374 --- [ntainer#0-0-C-1] c.c.kafka.consumer.MessageConsumer       : Received message with key : [1], value : [message 1], topic : [test-topic], offset : [1], at : [2022-07-26T00:26:43.493635]
   ```
