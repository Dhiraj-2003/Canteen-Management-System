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
    private ProductRepo productRepo;

    // Fetch all products
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // Add a new product
    public void addProduct(Product product) {
        productRepo.save(product);
    }

    // Remove product by ID
    public void removeProductById(Long id) {
        productRepo.deleteById(id);
    }

    // Get a product by its ID
    public Optional<Product> getProductById(Long id) {
        return productRepo.findById(id);
    }

    // Fetch all products by category ID
    public List<Product> getProductsByCategoryId(Integer categoryId) {
        return productRepo.findAllByCategory_Id(categoryId);
    }
}
