package com.dhiraj.canteen.Controller;


import com.dhiraj.canteen.Entity.Cart;
import com.dhiraj.canteen.Entity.Product;
import com.dhiraj.canteen.Entity.User;
import com.dhiraj.canteen.Global.GlobalData;
import com.dhiraj.canteen.Service.CartService;
import com.dhiraj.canteen.Service.ProductService;
import com.dhiraj.canteen.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id){
        GlobalData.findUserName();
        //write code for adding the product to cart of the user
        //we need Products Id and the user email
        Cart newCart = new Cart();
        newCart.setCartEmail(GlobalData.user_email);
        newCart.setProductId(id);
        cartService.addToCart(newCart);
        GlobalData.cart.add(productService.getProductById(id).get());
        System.out.println("Product added ");
        return "redirect:/shop";
    }

    @GetMapping("/cart")
    public String addToCart(Model model){
        GlobalData.cart.clear();
        GlobalData.findUserName();
        cartService.getCartItems();
        model.addAttribute("cartCount",GlobalData.cart.size());
        model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPprice).sum());
        model.addAttribute("cart",GlobalData.cart);
        return "cart";
    }

    @GetMapping("/cart/removeItem/{index}")
    public String removeItemFromCart(@PathVariable int index){
        Long productId = GlobalData.cart.get(index).getPid();
        Cart oldCart=new Cart();
        oldCart.setProductId(productId);
        oldCart.setCartEmail(GlobalData.user_email);
        GlobalData.cart.remove(index);
        cartService.removeFromCart(oldCart);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model){
        User user = userService.getUserByEmail(GlobalData.user_email);
        model.addAttribute("cartCount",GlobalData.cart.size());
        model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPprice).sum());
        model.addAttribute("amount", user.getAmount());
        return "checkout";
    }

    @GetMapping("/payNow")
    public String paynow(Model model){
        User user = userService.getUserByEmail(GlobalData.user_email);
        float mybal = user.getAmount();
        float total = (float) GlobalData.cart.stream().mapToDouble(Product::getPprice).sum();
        float bal = mybal - total;
        if(bal>=0){
            user.setAmount(bal);
            userService.addUser(user);
            model.addAttribute("tokenid",user.getId());
            return "paysuccess";
        }else{
            return "payfailed";
        }
    }

//    @GetMapping("/token")
//    public String token(Model model){
//        User user = userService.getUserByEmail(GlobalData.user_email);
//        model.addAttribute("tokenid",user.getId());
//        return "paysuccess";
//    }

}
