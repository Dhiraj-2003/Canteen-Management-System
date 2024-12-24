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
    CategoryDAO theCategoryDAO;

    public List<Category> getAllCategory(){
      return theCategoryDAO.findAll();
    }

    public void addCategory(Category theCategory){
        theCategoryDAO.save(theCategory);
    }

    public void removeCategoryById(Integer id){
        theCategoryDAO.deleteById(id);
    }

    public Optional<Category> findCategoryById(Integer id){
        return theCategoryDAO.findById(id);
    }
}
