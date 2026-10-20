package com.home.marketplace.db.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNT")
@Data
public class Account {
    private @Id
    @GeneratedValue
    Long id;
    @Column(name = "sum")
    BigDecimal sum;
}
