from kafka import KafkaConsumer
import sys

bootstrap_servers = ['localhost:29092']
topic_name = 'my_test_topic'

consumer = KafkaConsumer(
                         topic_name,
                         bootstrap_servers = bootstrap_servers,
                         auto_offset_reset = 'earliest',
                         enable_auto_commit = True,
                         group_id = 'my-group-1'
                        )

try:
    for message in consumer:
        print ("Message received - %s:%d:%d: key=%s value=%s" % (message.topic, message.partition, message.offset, message.key, message.value))
except KeyboardInterrupt:
    sys.exit()
