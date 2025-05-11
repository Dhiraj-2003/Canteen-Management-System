package com.dhiraj.canteen.Controller;

import com.dhiraj.canteen.DTO.OrderDTO;
import com.dhiraj.canteen.DTO.ProductDTO;
import com.dhiraj.canteen.Entity.Category;
import com.dhiraj.canteen.Entity.Order;
import com.dhiraj.canteen.Entity.OrderStatus;
import com.dhiraj.canteen.Entity.Product;
import com.dhiraj.canteen.Service.CategoryService;
import com.dhiraj.canteen.Service.ProductService;
import com.dhiraj.canteen.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    public static final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/productImages";

    @Autowired
    private CategoryService theCategoryService;

    @Autowired
    private ProductService theProductService;

    @Autowired
    private OrderService theOrderService;

    @GetMapping("/admin")
    public String adminHome() {
        return "adminHome";
    }

    // Categories

    @GetMapping("/admin/categories")
    public String showCategories(Model theModel) {
        theModel.addAttribute("categories", theCategoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getAddCategory(Model theModel) {
        theModel.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postAddCategory(@ModelAttribute("category") Category theCategory) {
        theCategoryService.addCategory(theCategory);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        theCategoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable Integer id, Model theModel) {
        Optional<Category> categoryData = theCategoryService.getCategoryById(id);
        if (categoryData.isPresent()) {
            theModel.addAttribute("category", categoryData.get());
            return "categoriesAdd";
        }
        return "404";
    }

    // Products

    @GetMapping("/admin/products")
    public String showProducts(Model theModel) {
        theModel.addAttribute("products", theProductService.getAllProducts());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String addProduct(Model theModel) {
        theModel.addAttribute("productDTO", new ProductDTO());
        theModel.addAttribute("categories", theCategoryService.getAllCategories());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String addProduct(
            @ModelAttribute("productDTO") ProductDTO theProductDTO,
            @RequestParam("productImage") MultipartFile theMultipartFile,
            @RequestParam("imgName") String imgName
    ) throws IOException {

        Product theProduct = new Product();
        theProduct.setPid(theProductDTO.getPid());
        theProduct.setPname(theProductDTO.getPname());
        theProduct.setPprice(theProductDTO.getPprice());
        theProduct.setPweight(theProductDTO.getPweight());
        theProduct.setPdescription(theProductDTO.getPdescription());
        theProduct.setCategory(theCategoryService.getCategoryById(theProductDTO.getCategoryId()).orElse(null));

        String imageUUID;
        if (!theMultipartFile.isEmpty()) {
            imageUUID = theMultipartFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, imageUUID);
            Files.write(filePath, theMultipartFile.getBytes());
        } else {
            imageUUID = imgName;
        }

        theProduct.setPimagename(imageUUID);
        theProductService.addProduct(theProduct);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        theProductService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable Long id, Model theModel) {
        Optional<Product> optionalProduct = theProductService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Product theProduct = optionalProduct.get();
            ProductDTO theProductDTO = new ProductDTO();

            theProductDTO.setPid(theProduct.getPid());
            theProductDTO.setPname(theProduct.getPname());
            theProductDTO.setCategoryId(theProduct.getCategory().getId());
            theProductDTO.setPprice(theProduct.getPprice());
            theProductDTO.setPweight(theProduct.getPweight());
            theProductDTO.setPdescription(theProduct.getPdescription());
            theProductDTO.setPimagename(theProduct.getPimagename());

            theModel.addAttribute("productDTO", theProductDTO);
            theModel.addAttribute("categories", theCategoryService.getAllCategories());
            return "productsAdd";
        }

        return "404";
    }

    // ✅ Manage Orders - View all orders
    @GetMapping("/admin/orders")
    public String viewOrders(Model model) {
        List<OrderDTO> orders = theOrderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders"; // Add this Thymeleaf template to display orders
    }

    // ✅ Update Order Status
    @PostMapping("/admin/orders/update/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId,
                                    @RequestParam("status") OrderStatus status) {
        theOrderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders"; // Redirect back to orders list
    }

    @GetMapping("/admin/order/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        theOrderService.deleteOrderById(id);
        return "redirect:/admin/orders"; // Redirect to the orders page after deletion
    }

    // ✅ Search Orders by Order ID or Token
    @GetMapping("/admin/orders/search")
    public String searchOrders(@RequestParam("keyword") String keyword, Model model) {
        List<Order> orders;
        try {
            orders = theOrderService.searchByOrderIdLike(keyword);
        } catch (Exception e) {
            orders = new ArrayList<>();
        }
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders";
    }
}
