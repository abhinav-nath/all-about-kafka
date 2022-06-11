https://docs.confluent.io/platform/current/installation/configuration/producer-configs.html

## acks

The number of acknowledgments the producer requires the leader to have received before considering a request complete.

This controls the durability of records that are sent. The following settings are allowed:

values - 0, 1 or all

1   ->  (default) producer send() call guarantees that message is written to the Leader.
        The leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers.

all ->  send() call is considered successful when the message is written to the Leader and to all the Followers.
        The leader will wait for the full set of in-sync replicas to acknowledge the record.
        This guarantees that the record will not be lost as long as at least one in-sync replica remains alive.

0   ->  no guarantee (not recommended). The producer will not wait for any acknowledgment from the server at all.


## retries

Number of retries in case of any failure while producing messages to Kafka

integer value [ 0 - 2147483647 ]

In Spring Kafka, the default value is the max value i.e. 2147483647


## retry.backoff.ms

The amount of time to wait before attempting to retry a failed request to a given topic partition.
This avoids repeatedly sending requests in a tight loop under some failure scenarios.

Integer value in milliseconds

Default value is 100ms
