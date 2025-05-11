package com.dhiraj.canteen.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pid")
    private Long pid;

    @Column(name="pname")
    private String pname;

    @Column(name="pprice")
    private double pprice;

    @Column(name="pweight")
    private double pweight;

    @Column(name="pdescription")
    private String pdescription;

    @Column(name="pimagename")
    private String pimagename;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "cid")
    private Category category;
}
