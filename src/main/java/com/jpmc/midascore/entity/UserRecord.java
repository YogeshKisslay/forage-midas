package com.jpmc.midascore.entity;
import java.util.Set; // <-- Add this import
import jakarta.persistence.*;

@Entity
public class UserRecord {

    @Id
    @GeneratedValue()
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float balance;

    // 👇 ADD THESE TWO FIELDS
    @OneToMany(mappedBy = "sender")
    private Set<TransactionRecord> sentTransactions;

    @OneToMany(mappedBy = "recipient")
    private Set<TransactionRecord> receivedTransactions;


    protected UserRecord() {
    }

    public UserRecord(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', balance='%f'", id, name, balance);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
