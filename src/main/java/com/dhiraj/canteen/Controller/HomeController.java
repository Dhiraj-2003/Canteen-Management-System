package com.dhiraj.canteen.Controller;

import com.dhiraj.canteen.Global.GlobalData;
import com.dhiraj.canteen.Service.CartService;
import com.dhiraj.canteen.Service.CategoryService;
import com.dhiraj.canteen.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private CategoryService theCategoryService;

    @Autowired
    private ProductService theProductService;

    @Autowired
    private CartService cartService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "redirect:/shop";
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        GlobalData.cart.clear();
        GlobalData.findUserName();
        cartService.loadCartItems();

        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("categories", theCategoryService.getAllCategories());
        model.addAttribute("products", theProductService.getAllProducts());

        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String getProductByCategory(Model model, @PathVariable Integer id) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("categories", theCategoryService.getAllCategories());
        model.addAttribute("products", theProductService.getProductsByCategoryId(id));

        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable Long id) {
        theProductService.getProductById(id).ifPresent(product -> model.addAttribute("product", product));
        model.addAttribute("cartCount", GlobalData.cart.size());

        return "viewProduct";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
