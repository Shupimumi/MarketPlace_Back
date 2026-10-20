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
public class Order {

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

    public Order() {
    }

    public Order(String description, Status status, List<Good> goods) {
        this.description = description;
        this.status = status;
        this.goods.addAll(goods != null ? goods : new ArrayList<>());
    }


    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Order order))
            return false;
        return Objects.equals(this.id, order.id) && Objects.equals(this.description, order.description)
                && this.status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.description, this.status);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id + ", description='" + this.description + '\'' + ", status=" + this.status + '}';
    }
}
