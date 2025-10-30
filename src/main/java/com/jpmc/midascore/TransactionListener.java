package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    /**
     * Listens for incoming messages on the configured Kafka topic and deserializes them
     * into the Transaction object.
     * The topic name is read from the Spring environment variable (e.g., application.yml).
     */
    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        // Log the received transaction. This is where you would set your breakpoint.
        logger.info("Received transaction for validation: {}", transaction);
    }
}