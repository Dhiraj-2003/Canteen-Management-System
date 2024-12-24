package com.dhiraj.canteen.DAO;


import com.dhiraj.canteen.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart,Integer> {
    //nothing to do
    public List<Cart> findByCartEmail(String emailparam);
}
