package com.home.marketplace.db.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CLIENT")
@Data
public class ClientEntity {
    private @Id
    @GeneratedValue
    Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "address")
    private String address;

    public ClientEntity(String name, String surname, String address) {
        this.name = name;
        this.surname = surname;
        this.address = address;
    }
}
