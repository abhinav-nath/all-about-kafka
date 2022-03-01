package com.codecafe.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {

    private final String consumerName;
    private final String bootstrapServers;
    private final String groupId;
    private final String topics;
    private Properties props;

    public Consumer(String consumerName, String bootstrapServers, String groupId, String topics) {
        this.consumerName = consumerName;
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.topics = topics;
        setConsumerProperties();
    }

    private void setConsumerProperties() {
        props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // if we want to adjust defaults
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // default is latest
        // props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false) // default 5000 - change how often to commit offsets
        // props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 10000) // default 5000 - change how often to commit offsets
    }

    public void consumeMessages() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(this.topics));

        ConsumerRecords<String, String> messages = consumer.poll(Duration.ofSeconds(30));

        messages.forEach(this::print);

        consumer.close();
    }

    private void print(ConsumerRecord<String, String> message) {
        System.out.printf("%s - key: [%s] value: [%s] partition: [%d] offset: [%d]%n",
                consumerName, message.key(), message.value(), message.partition(), message.offset());
    }

}