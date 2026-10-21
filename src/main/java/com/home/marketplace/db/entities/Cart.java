package com.home.marketplace.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CART")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_id", nullable = false, unique = true)
    private Long client_id;
    @OneToMany(mappedBy = "cart")
    @JoinColumn(name = "cart_item.cart_id")
    private Set<CartItem> cartItem = new HashSet<>();
}
