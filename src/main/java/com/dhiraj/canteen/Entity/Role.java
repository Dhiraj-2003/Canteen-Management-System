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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Foreign key pointing to User
    private User user;

    public Role(String role, String email, User user) {
        this.role = role;
        this.email = email;
        this.user = user;
    }
}
