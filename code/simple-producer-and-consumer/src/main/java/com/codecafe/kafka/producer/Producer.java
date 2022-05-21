package com.codecafe.kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer {

  private final String bootstrapServers;
  private final String topics;
  private Properties props;

  public Producer(String bootstrapServers, String topics) {
    this.bootstrapServers = bootstrapServers;
    this.topics = topics;
    setProducerProperties();
  }

  private void setProducerProperties() {
    props = new Properties();
    this.props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    this.props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    this.props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
  }

  public void produceMessages() {
    KafkaProducer<String, String> producer = new KafkaProducer<>(props);
    for (int i = 0; i < 10; i++) {
      producer.send(new ProducerRecord<>(topics, "apple", "this is message #" + i));
    }
    producer.close();
  }

}