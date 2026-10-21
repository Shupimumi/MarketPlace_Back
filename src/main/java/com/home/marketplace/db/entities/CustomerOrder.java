package com.home.marketplace.db.entities;

import com.home.marketplace.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER_ORDER")
@Data
public class CustomerOrder {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "order")
    private List<Good> goods = new ArrayList<>();
}
