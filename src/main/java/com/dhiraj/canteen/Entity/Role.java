package com.dhiraj.canteen.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="role")
    private String role;

    @Column(name="email")
    private String email;

    public Role(String role, String email){
        this.role=role;
        this.email=email;
    }

    public Role (){

    }
}
