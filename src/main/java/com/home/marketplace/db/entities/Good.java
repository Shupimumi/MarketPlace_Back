package com.home.marketplace.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "GOOD")
@Getter
@Setter
@NoArgsConstructor
public class Good {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "preName")
    private String preName;
    @Column(name = "postName")
    private String postName;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "customer_order_id")
    private Long customerOrderId;
}
