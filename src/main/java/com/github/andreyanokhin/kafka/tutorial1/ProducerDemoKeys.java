package com.github.andreyanokhin.kafka.tutorial1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

//        create Producer properties
        String bootstrapServers = "127.0.0.1:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

//        create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);


        for (int i=0; i < 10; i++) {

            String topic = "first_topic";
            String value = "hello world " + i;
            String key = "id_" + i;

            //        create a Producer record
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            logger.info("Key: " + key);

            //        send data - asynchronous
            producer.send(record, (recordMetadata, e) -> {
                //                executes every time a record is successfully sent or an exception is thrown
                if (e == null) {
                    //                    the record was successfully sent
                    logger.info("Received new metadata. \n" +
                            "Topic:" + recordMetadata.topic() + "\n" +
                            "Partition:" + recordMetadata.partition() + "\n" +
                            "Offset:" + recordMetadata.offset() + " \n" +
                            "Timestamp:" + recordMetadata.timestamp()
                    );
                } else {
                    logger.error("Error while producing", e);
                }
            });
        }
//
        producer.flush();
        producer.close();
    }
}
