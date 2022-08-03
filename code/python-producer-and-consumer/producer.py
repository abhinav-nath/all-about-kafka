from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers='localhost:29092')

future = producer.send('my_test_topic', b'Hello World!')

metadata = future.get()

print(metadata.topic)
print(metadata.partition)
