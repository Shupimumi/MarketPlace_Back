package com.home.marketplace.db.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "GOOD")
@Data
public class Good {

    private @Id
    @GeneratedValue
    Long id;

    @Column(name = "preName")
    private String preName;

    @Column(name = "postName")
    private String postName;

    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder customerOrder;
}
