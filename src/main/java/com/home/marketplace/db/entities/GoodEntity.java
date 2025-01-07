package com.home.marketplace.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;


@Setter
@Getter
@Entity
@Table(name = "goods")
public class GoodEntity {

    private @Id
    @GeneratedValue
    Long id;

    @Column(name = "preName")
    private String preName;

    @Column(name = "postName")
    private String postName;

    @Column(name = "cost")
    private BigDecimal cost;

    GoodEntity() {
    }

    public GoodEntity(String preName, String postName, BigDecimal cost) {
        this.preName = preName;
        this.postName = postName;
        this.cost = cost;
    }

    public String getName() {
        return this.preName + " " + this.postName;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.preName = parts[0];
        this.postName = parts[1];
    }

    @Override
    public String toString() {
        return "Good: id = " + this.id + ", preName = " + this.preName + ", postName = " + this.postName + ", cost = " + this.cost;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof GoodEntity goodEntity))
            return false;
        return Objects.equals(this.id, goodEntity.id)
                && Objects.equals(this.preName, goodEntity.preName)
                && Objects.equals(this.postName, goodEntity.postName)
                && Objects.equals(this.cost, goodEntity.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.preName, this.postName, this.cost);
    }
}
