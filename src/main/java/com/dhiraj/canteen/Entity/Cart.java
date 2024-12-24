package com.dhiraj.canteen.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="cart_email")
    private String cartEmail;

    @Column(name = "product_id")
    private Long productId;
}
