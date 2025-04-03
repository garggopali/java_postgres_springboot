package com.gopali.gift_card_springboot_postgred.giftcards;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gift_cards")
@Getter
@Setter
@Data  // Lombok generates Getters, Setters, toString, etc.

public class GiftCards {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // Primary Key

    @Column(name = "card_number", length = 16, unique = true, nullable = false)
    private String cardNumber;  // 16-digit unique gift card number

    @Column(name = "redemption_code", length = 16, unique = true, nullable = false)
    private String redemptionCode;  // 16-character unique hashed redemption code
    
    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Column(name = "balance", precision = 10, scale = 2, nullable = false)
    private BigDecimal balance;  // Current balance

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;  // Currency (e.g., "USD")

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GiftCardsStatus status;  // Card status (ACTIVE, EXPIRED, BLOCKED)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // Timestamp when created

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // Last update timestamp

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Ensure balance is never null
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with balance initialization
    public GiftCards() {
        this.balance = BigDecimal.ZERO; // Prevents null balance
        this.createdAt = LocalDateTime.now();
    }
    


    // Method to generate a unique 16-digit card number
    public void generateCardNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            number.append(random.nextInt(10));
        }
        this.cardNumber = number.toString();
    }

    // Method to generate a hashed 16-character alphanumeric redemption code
    public void generateRedemptionCode() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            code.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }
        this.redemptionCode = code.toString();  // You can hash this if needed
    }

    
    // Getters and Setters with notation
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getRedemptionCode() {
        return redemptionCode;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {  // ✅ FIX: This method was missing
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {  // ✅ FIX: This method was missing
        this.currency = currency;
    }

    public GiftCardsStatus getStatus() {
        return status;
    }
    
    public void setStatus(GiftCardsStatus status) {  // ✅ FIX: This method was missing
        this.status = status;
    }
}
