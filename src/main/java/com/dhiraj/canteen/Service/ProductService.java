package com.dhiraj.canteen.Service;

import com.dhiraj.canteen.DAO.ProductRepo;
import com.dhiraj.canteen.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo theProductRepo;

    public List<Product> getAllProducts(){
        return theProductRepo.findAll();
    }

   public void addProduct(Product theProduct){
       theProductRepo.save(theProduct);
    }

    public void removeProductById(Long id){
        theProductRepo.deleteById(id);
    }

    public Optional<Product> getProductById(Long id){
        return theProductRepo.findById(id);
    }

    public List<Product> getAllProductsByCategoryId(Integer id){
        return theProductRepo.findAllByCategory_Id(id);
    }
}
