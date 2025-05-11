package com.dhiraj.canteen.Service;

import com.dhiraj.canteen.DAO.CategoryDAO;
import com.dhiraj.canteen.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public void addCategory(Category category) {
        categoryDAO.save(category);
    }

    public void removeCategoryById(Integer id) {
        categoryDAO.deleteById(id);
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryDAO.findById(id);
    }
}
