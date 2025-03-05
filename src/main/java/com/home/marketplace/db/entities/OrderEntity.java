package com.home.marketplace.db.entities;

import com.home.marketplace.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER_ORDER")
@Data
public class OrderEntity {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "order")
    private List<GoodEntity> goods;

    OrderEntity() {
    }

    public OrderEntity(String description, Status status) {
        this.description = description;
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof OrderEntity order))
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
