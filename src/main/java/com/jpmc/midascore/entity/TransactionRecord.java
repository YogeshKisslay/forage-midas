package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private UserRecord sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    // 👇 ADD THIS FIELD
    @Column(nullable = false)
    private float incentiveAmount;

    // Required for JPA
    protected TransactionRecord() {
    }

    // 👇 UPDATE CONSTRUCTOR
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentiveAmount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentiveAmount = incentiveAmount; // <-- NEW
    }
    // Getters
    public long getId() {
        return id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public float getAmount() {
        return amount;
    }

    // 👇 ADD GETTER
    public float getIncentiveAmount() {
        return incentiveAmount;
    }
}