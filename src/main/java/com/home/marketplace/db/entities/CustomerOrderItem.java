package com.home.marketplace.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CUSTOMER_ORDER_ITEM")
public class CustomerOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_order_id", nullable = false)
    @JsonIgnore
    private CustomerOrder customerOrder;
    @Column(name = "good_id", nullable = false)
    private Long goodId;
    @Column(name = "good_name", nullable = false)
    private String goodName;
    @Column(name = "cost", nullable = false)
    private BigDecimal cost;
    @Column(name = "quantity", nullable = false)
    private int quantity;
}
