package org.springboot.authapi.Enities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @Column(name = "orderItem")
    private List<OrderItem> items;

    @Column(name = "totalPrice")
    private Double totalPrice;

    @Column(name = "status")
    private String status; //PAID,SHIPPED,PENDING
}
