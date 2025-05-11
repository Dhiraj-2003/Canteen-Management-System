package com.dhiraj.canteen.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cid")
    private int id;

    @Column(name="cname")
    private String name;

}
