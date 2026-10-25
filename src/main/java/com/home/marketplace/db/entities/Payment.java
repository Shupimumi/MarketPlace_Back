package com.home.marketplace.db.entities;

import com.home.marketplace.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "PAYMENT")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    @Column(name = "customer_order_id", nullable = false)
    private Long customerOrderId;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}
