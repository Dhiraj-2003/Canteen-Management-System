package com.dhiraj.canteen.DAO;

import com.dhiraj.canteen.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category,Integer> {
    //nothing have to do
}
