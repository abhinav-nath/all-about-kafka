from kafka import KafkaProducer

bootstrap_servers = ['localhost:29092']
topic_name = 'my_test_topic'

producer = KafkaProducer(bootstrap_servers = bootstrap_servers)

future = producer.send(topic_name, key=b'1', value=b'This is message 1')

metadata = future.get()

print('message sent successfully on topic %s partition %s' % (metadata.topic, metadata.partition))
