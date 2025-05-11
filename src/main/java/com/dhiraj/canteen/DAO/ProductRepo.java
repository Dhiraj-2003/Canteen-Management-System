package com.dhiraj.canteen.DAO;

import com.dhiraj.canteen.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory_Id(Integer id);

    Product findByPname(String pname);
}
