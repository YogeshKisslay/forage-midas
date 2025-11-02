package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // <-- Import this
import org.springframework.web.client.RestTemplate; // <-- NEW: Import this
import org.springframework.beans.factory.annotation.Autowired; // <-- NEW
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseConduit {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConduit.class);
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRepository; // <-- Add this

    // 👇 NEW: RestTemplate is used for the API call
    private final RestTemplate restTemplate;
    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive"; // <-- NEW

    // Update the constructor (Add RestTemplate)
    public DatabaseConduit(UserRepository userRepository,
                           TransactionRecordRepository transactionRepository,
                           RestTemplate restTemplate) { // <-- NEW PARAMETER
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate; // <-- Save the RestTemplate
    }
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    /**
     * Processes a transaction.
     * This method is @Transactional, meaning all database operations within it
     * will either complete successfully or fail together (atomicity).
     */
    @Transactional
    public void processTransaction(Transaction transaction) {
//        // 1. Find sender and recipient (using your resolved logic)
//        UserRecord sender = userRepository.findById(transaction.getSenderId());
//        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // 👇 CHANGE THESE TWO LINES to use Optional.orElse(null)
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);

        // 2. Validate transaction (no changes needed here)
        if (sender == null || recipient == null || sender.getBalance() < transaction.getAmount()) {
            // Log warning and return (discard transaction)
            return;
        }

        // 3. Post transaction to Incentives API to get incentive amount
        Incentive incentive = restTemplate.postForObject(
                INCENTIVE_API_URL,
                transaction, // Pass the Transaction DTO directly
                Incentive.class // Expect the Incentive DTO back
        );

        float incentiveAmount = 0.0f;
        if (incentive != null) {
            incentiveAmount = incentive.getAmount();
        }

        // 4. Update balances: Sender loss, Recipient gain + Incentive
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        // 5. Save updated users and the new transaction record
        userRepository.save(sender);
        userRepository.save(recipient);

        // Note: TransactionRecord needs a field for incentiveAmount (See Step 4)
        TransactionRecord record = new TransactionRecord(
                sender,
                recipient,
                transaction.getAmount(),
                incentiveAmount // Pass the incentive amount
        );
        transactionRepository.save(record);

        logger.info("Processed transaction: {} from sender {} to recipient {}. Incentive: {}",
                transaction.getAmount(), sender.getId(), recipient.getId(), incentiveAmount);
    }
}